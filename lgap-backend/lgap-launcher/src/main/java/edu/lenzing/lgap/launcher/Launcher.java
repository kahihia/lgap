package edu.lenzing.lgap.launcher;

import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.json.DecodeException;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;

import java.util.Scanner;

/**
 * @author Csaba Ilonka <Ilonka.Csaba@isi-hagenberg.at>
 */
public class Launcher extends io.vertx.core.Launcher {

    private static final Logger LOG = LoggerFactory.getLogger(Launcher.class);

    public static void main(String[] args) {
        new Launcher().dispatch(args);
    }

    @Override
    public void afterStartingVertx(final Vertx vertx) {
        super.afterStartingVertx(vertx);

        LOG.info("Vert.x launched successfully");

    }

    @Override
    public void beforeDeployingVerticle(final DeploymentOptions deploymentOptions) {
        super.beforeDeployingVerticle(deploymentOptions);

        // Load config file(s)

        if (deploymentOptions.getConfig() == null) {
            deploymentOptions.setConfig(new JsonObject());
        }

        final JsonObject config = this.readConfigFile("config/config.json");
        deploymentOptions.getConfig().mergeIn(config);

    }

    @Override
    public void handleDeployFailed(Vertx vertx, String mainVerticle, DeploymentOptions deploymentOptions, Throwable cause) {
        super.handleDeployFailed(vertx, mainVerticle, deploymentOptions, cause);

        // TODO: shut down the application

    }

    private JsonObject readConfigFile(final String configFilePath) {

        JsonObject config = new JsonObject();

        LOG.info(String.format("Loading config file '%s'", configFilePath));

        try (Scanner scanner = new Scanner(this.getClass().getClassLoader().getResourceAsStream(configFilePath)).useDelimiter("\\A")) {
            config = new JsonObject(scanner.next());
        } catch (final DecodeException ex) {
            LOG.error(String.format("Config file '%s' does not have a valid JSON content", configFilePath), ex);
        } catch (final NullPointerException ex) {
            LOG.error(String.format("Config file '%s' does not exist", configFilePath));
        }

        return config;

    }

}
