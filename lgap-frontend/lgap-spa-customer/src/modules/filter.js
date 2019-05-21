import Vue from 'vue';

Vue.filter('datetime', (datetime) => {
    return datetime.format('dddd, MMMM Do YYYY, [at] HH:mm:ss');
});

Vue.filter('fromnow', (moment) => {
    return moment.fromNow();
});

Vue.filter('descriptiveTypeName', (type) => {
    switch (type) {
        case "DUTCH":   return "Descending price auction";
        case "ENGLISH": return "Ascending price auction";
        default:        return type;
    }
});