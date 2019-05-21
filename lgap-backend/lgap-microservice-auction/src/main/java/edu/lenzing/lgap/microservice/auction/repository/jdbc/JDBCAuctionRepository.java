package edu.lenzing.lgap.microservice.auction.repository.jdbc;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import edu.lenzing.lgap.microservice.auction.model.Auction;
import edu.lenzing.lgap.microservice.auction.repository.AuctionRepository;
import edu.lenzing.lgap.microservice.common.model.UserRegion;
import edu.lenzing.lgap.microservice.common.repository.BaseJDBCEntityRepository;
import edu.lenzing.lgap.microservice.common.repository.exception.RepositoryException;
import io.vertx.core.CompositeFuture;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.sql.SQLConnection;

import java.util.*;

/**
 * @author Csaba Ilonka <Ilonka.Csaba@isi-hagenberg.at>
 */
@Singleton
public class JDBCAuctionRepository extends BaseJDBCEntityRepository<Auction> implements AuctionRepository {

    private static final Logger LOG = LoggerFactory.getLogger(JDBCAuctionRepository.class);

    private static final String SQL_INSERT_AUCTION = "INSERT INTO `auction` (`name`, `description`, `startDateTimestamp`, `endDateTimestamp`, `reservePrice`, `auctionType`, `auctionPhase`) VALUES (?, ?, ?, ?, ?, ?, ?);";
    private static final String SQL_INSERT_AUCTION_REGION = "INSERT INTO `auction-region` (`auctionId`, `region`) VALUES (?, ?);";


    @Inject
    public JDBCAuctionRepository(Vertx vertx, @Named("VertxConfig") JsonObject config) {
        super(vertx, config, Auction.class);
    }

    @Override
    public Future<Optional<Auction>> getById(final Long id) {
        return RepositoryException.failedFuture("Operation not supported");
    }

    @Override
    public Future<Auction> save(final Auction auction) {
        throw new RepositoryException("Not yet implement");
    }

    @Override
    public Future<Auction> save(final Auction auction, final SQLConnection transaction) {

        final Future<Auction> future = Future.future();

        final JsonArray auctionParams = new JsonArray()
                .add(auction.getName())
                .add(auction.getDescription())
                .add(auction.getStartTimestamp())
                .add(auction.getEndTimestamp())
                .add(auction.getReservePrice())
                .add(auction.getAuctionType())
                .add(auction.getAuctionPhase());

        saveEntity(transaction, SQL_INSERT_AUCTION, auctionParams, auction).setHandler(auctionHandler -> {

            if (auctionHandler.succeeded()) {

                final Auction persistedAuction = auctionHandler.result();

                final List<JsonArray> regionParams = new LinkedList<>();
                for (final UserRegion region : auction.getRegions()) {
                    regionParams.add(new JsonArray()
                            .add(persistedAuction.getId())
                            .add(region)
                    );
                }

                transaction.batchWithParams(SQL_INSERT_AUCTION_REGION, regionParams, regionHandler -> {
                    if (regionHandler.succeeded()) {
                        future.complete(persistedAuction);
                    } else {
                        LOG.error(String.format("Failed to persist regions for auction [%d]", persistedAuction.getId()), regionHandler.cause());
                        future.fail(regionHandler.cause());
                    }
                });

            } else {
                future.fail(auctionHandler.cause());
            }

        });

        return future;

    }

    @Override
    public Future<Auction> update(final Auction entity) {
        throw new RepositoryException("Not yet implement");
    }

    @Override
    public Future<Auction> update(final Auction entity, final SQLConnection transaction) {
        throw new RepositoryException("Not yet implement");
    }

    @Override
    public Future<Set<UserRegion>> getRegionsByAuctionId(final Long auctionId) {

        final Future<Set<UserRegion>> future = Future.future();

        retrieveAll("SELECT `region` FROM `auction-region` WHERE `auctionId` = ?;", new JsonArray().add(auctionId), handler -> {

            if (handler.succeeded()) {

                final Set<UserRegion> regions = new HashSet<>();

                for (final JsonObject json : handler.result()) {
                    regions.add(UserRegion.valueOf(json.getString("region")));
                }

                future.complete(regions);

            } else {
                future.fail(handler.cause());
            }

        });

        return future;

    }

    /**
     * filter: {
     *     text: String
     *     auctionType: String
     *     auctionPhase: String
     *     startDate: {
     *         timestamp: Long
     *         action:    [ BEFORE, ON, AFTER ]
     *     }
     *     endDate: {
     *         timestamp: Long
     *         action:    [ BEFORE, ON, AFTER ]
     *     }
     * }
     */
    @Override
    public Future<List<Auction>> search(final JsonObject filter) {

        final Future<List<Auction>> future = Future.future();

        final DynamicSearchQueryBuilder.DynamicQuery query = this.generateSearchQuery(filter);

        LOG.info(query.getQuery());
        LOG.info(query.getParams());

        final Map<Auction, Future> regionFutures = new HashMap<>();

        getAllEntities(query.getQuery(), query.getParams(), Auction.class).setHandler(handler -> {

            if (handler.succeeded()) {
                for (final Auction auction : handler.result()) {
                    regionFutures.put(auction, this.getRegionsByAuctionId(auction.getId()));
                }
                CompositeFuture.all(new ArrayList<>(regionFutures.values())).setHandler(compositeHandler -> {
                    if (compositeHandler.succeeded()) {
                        final List<Auction> auctions = new ArrayList<>();
                        for (final Map.Entry<Auction, Future> entry : regionFutures.entrySet()) {
                            final Future<Set<UserRegion>> rgns = entry.getValue();
                            entry.getKey().setRegions(rgns.result());
                            auctions.add(entry.getKey());
                        }
                        future.complete(auctions);
                    } else {
                        future.fail(compositeHandler.cause());
                    }
                });
            } else {
                future.fail(handler.cause());
            }

        });

        return future;
    }

    private DynamicSearchQueryBuilder.DynamicQuery generateSearchQuery(final JsonObject filter) {

        final DynamicSearchQueryBuilder queryBuilder = new DynamicSearchQueryBuilder();

        if (filter.getString("text") != null) {
            queryBuilder.put("(`name` LIKE '%' ? '%' OR `description` LIKE '%' ? '%')", filter.getString("text"), filter.getString("text"));
        }

        if (filter.getString("auctionType") != null) {
            queryBuilder.put("(`auctionType` = ?)", filter.getString("auctionType"));
        }

        if (filter.getString("auctionPhase") != null) {
            queryBuilder.put("(`auctionPhase` = ?)", filter.getString("auctionPhase"));
        }

        if (filter.getJsonObject("startDate") != null) {
            final Long timestamp = filter.getJsonObject("startDate").getLong("timestamp");
            switch (filter.getJsonObject("startDate").getString("action")) {
                case "BEFORE":
                    queryBuilder.put("(`startDateTimestamp` < ?)", timestamp);
                    break;
                case "ON":
                    queryBuilder.put("(`startDateTimestamp` = ?)", timestamp);
                    break;
                case "AFTER":
                    queryBuilder.put("(`startDateTimestamp` > ?)", timestamp);
                    break;
            }

        }

        if (filter.getJsonObject("endDate") != null) {
            final Long timestamp = filter.getJsonObject("endDate").getLong("timestamp");
            switch (filter.getJsonObject("endDate").getString("action")) {
                case "BEFORE":
                    queryBuilder.put("(`endDateTimestamp` < ?)", timestamp);
                    break;
                case "ON":
                    queryBuilder.put("(`endDateTimestamp` = ?)", timestamp);
                    break;
                case "AFTER":
                    queryBuilder.put("(`endDateTimestamp` > ?)", timestamp);
                    break;
            }

        }

//        if (filter.getJsonObject("endDate") != null) {
//            queryBuilder.put("(`auctionPhase` = '?')", filter.getString("auctionPhase"));
//        }

        return queryBuilder.generate("SELECT * FROM `auction`");

    }

    private final class DynamicSearchQueryBuilder {

        public class QueryFragment {

            private final String sqlFragment;
            private final JsonArray queryParams;

            public QueryFragment(final String sqlFragment, final Object... params) {

                this.sqlFragment = sqlFragment;
                this.queryParams = new JsonArray();

                for (final Object param : params) {
                    queryParams.add(param);
                }

            }

            public String getSqlFragment() {
                return sqlFragment;
            }

            public JsonArray getQueryParams() {
                return queryParams;
            }
        }

        public class DynamicQuery {

            private final String query;
            private final JsonArray params;

            public DynamicQuery(final String query, final JsonArray params) {
                this.query = query;
                this.params = params;
            }

            public String getQuery() {
                return query;
            }

            public JsonArray getParams() {
                return params;
            }
        }

        private final List<QueryFragment> fragments;

        public DynamicSearchQueryBuilder() {
            this.fragments = new LinkedList<>();
        }

        public void put(final String sqlFragment, final Object... params) {
            fragments.add(new QueryFragment(sqlFragment, params));
        }

        public DynamicQuery generate(final String baseQuery) {

            final StringBuilder builder = new StringBuilder(baseQuery);
            boolean isFirstFragment = true;

            final JsonArray params = new JsonArray();

            for (final QueryFragment fragment : fragments) {

                if (isFirstFragment) {
                    builder.append(" WHERE ");
                    isFirstFragment = false;
                } else {
                    builder.append(" AND ");
                }

                builder.append(fragment.getSqlFragment());
                params.addAll(fragment.getQueryParams());

            }

            builder.append(";");

            return new DynamicQuery(builder.toString(), params);

        }
    }
}
