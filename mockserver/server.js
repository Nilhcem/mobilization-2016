var fs = require('fs');
var express = require('express');
var app = express();
var util = require('util');

app.set('port', process.env.PORT || 8990);
app.use(express.static(__dirname + '/public'));

var port = app.get('port');
var defaultCharset = 'utf8';

/* Replaces 'localhost:8080' url with actual server url */
function replaceUrl(data, req) {
    var host = req.protocol + '://' + req.hostname + (port == 80 || port == 443 ? '' : ':' + port);
    return data.replace(/http:\/\/localhost:8080/g, host);
}

/* Gets a list of speakers, replacing urls  */
function getSpeakersFromFile(fileName, req) {
    var data = fs.readFileSync(fileName, defaultCharset);
    var speakers = JSON.parse(data);

    for (var i in speakers) {
	speakers[i].photo = replaceUrl(speakers[i].photo, req);
    }
    return JSON.stringify(speakers);
}

/* Sessions list */
app.get('/sessions', function(req, res) {
    util.log("GET /sessions");
    res.type('application/json; charset=' + defaultCharset);
    res.status(200).send(fs.readFileSync('data/sessions.json', defaultCharset));
});

/* Speakers */
app.get('/speakers', function(req, res) {
    util.log("GET /speakers");
    sleep(400);
    res.type('application/json; charset=' + defaultCharset);
    res.status(200).send(getSpeakersFromFile('data/speakers.json', req));
});

/* Default (home page) */
app.get('^*$', function(req, res) {
    var data = fs.readFileSync('data/index.html', defaultCharset);
    data = replaceUrl(data, req);

    res.type('text/html; charset=' + defaultCharset);
    res.status(200).send(data);
});

/* Sleep for ${durationMillis} ms (only for testing a slow network call) */
function sleep(durationMillis) {
    var now = new Date().getTime();
    while(new Date().getTime() < now + durationMillis) {
	// do nothing
    }
}

/* Starts server */
app.listen(port, function () {
    util.log('Express server listening on port ' + port);
});
