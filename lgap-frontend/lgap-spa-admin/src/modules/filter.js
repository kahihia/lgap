import Vue from 'vue';

Vue.filter('datetime', (datetime) => {
    return datetime.format('dddd, MMMM Do YYYY, [at] HH:mm:ss');
});

Vue.filter('fromnow', (moment) => {
    return moment.fromNow();
});

Vue.filter('shortRegionName', (region) => {
    switch (region) {
        case "EU_US":      return "EU & US";
        case "NORTH_ASIA": return "Asia";
        case "AMEA":       return "AMEA";
        default:           return region;
    }
});

Vue.filter('fullRegionName', (region) => {
    switch (region) {
        case "EU_US":      return "Europe and the United States";
        case "NORTH_ASIA": return "North Asia";
        case "AMEA":       return "Asia, Middle East and Africa";
        default:           return region;
    }
});

Vue.filter('descriptiveMethodName', (method) => {
    switch (method) {
        case "DUTCH":   return "Descending price auction";
        case "ENGLISH": return "Ascending price auction";
        default:        return method;
    }
});

Vue.filter('simpleMethodName', (method) => {
    switch (method) {
        case "DUTCH":   return "Dutch auction";
        case "ENGLISH": return "English auction";
        default:        return method;
    }
});