var app = require('express')();
var http = require('http').Server(app);
var io = require('socket.io')(http);
var fs = require('fs');
var Parse = require('parse').Parse;

var __dirName = "C:/Users/Edwin Kurniawan/Desktop/node";
var container = document.getElementById("photos");

var Files = Parse.Object.extend("Files");
Parse.initialize("hTu24zAXg8Tti9oK6W1e1uw36RzC4u3Ar2Ba6pXp", "DVkYzczzYgWJXryf6nuqJtzUr5cIa1cKyjhkBDHy");

var Files = Parse.Object.extend("Files");

app.get('/', function(req, res) {
	res.sendfile('browse_image.html');
});

function getAllImageFromParse() {
	var query = Parse.Query(Files);
	query.find( {
		success: function(result) {
			console.log("Found " + result.length + " pictures.");
			for(var i = 0; i < result.length; i++) {
				var imageb64 = result[i].get('base64');
				var img = new Image();
				img.src = 'data:image/jpeg;base64,' + imageb64;
				container.appendChild(img);
			}
		},
		error: function(result, error) {
			console.log("Error Getting Result");
		}
	});
}
getAllImageFromParse();
io.on('connection', function(socket) {
	console.log('A User Connected');
	socket.on("disconnect", function() {
		console.log("A User Disconnected");
	});

	socket.on("Test", function(data) {
		console.log(data);
	});

	socket.on("Image", function(data) {
		var fileName = __dirName + '/tmp/' + "img.jpeg";
		var decodedImage = new Buffer(data.buffer, 'base64');
		fs.writeFile("image.jpg", decodedImage, 'binary', function(err) {});
		var itemFiles = new Files();
		itemFiles.set("base64", data.buffer);
		itemFiles.set("arText", data.text);
		itemFiles.save(null, {
			success: function(itemFiles) {
				console.log("Data Uploaded to Parse");
			},
			error: function(itemFiles, error) {
				console.log("Data Can't be Uploaded to Parse");
			}
		});
		console.log("Added Image");
	});
});

http.listen(3000, function() {
	console.log('listening to Port 3000');
})