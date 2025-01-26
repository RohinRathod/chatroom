"use strict";

var usernamePage = document.querySelector("#username-page");
var chatPage = document.querySelector("#chat-page");
var usernameForm = document.querySelector("#usernameForm");
var messageForm = document.querySelector("#messageForm");
var messageInput = document.querySelector("#message");
var messageArea = document.querySelector("#messageArea");
var connectingElement = document.querySelector(".connecting");
var connectingElement = document.querySelector(".connecting");

var stompClient = null;
var username =  document.querySelector("#usernameraw").textContent;
 
//mycode
var password = null;

var colors = [
  "#2196F3",
  "#32c787",
  "#00BCD4",
  "#ff5652",
  "#ffc107",
  "#ff85af",
  "#FF9800",
  "#39bbb0",
  "#fcba03",
  "#fc0303",
  "#de5454",
  "#b9de54",
  "#54ded7",
  "#54ded7",
  "#1358d6",
  "#d611c6",
];

function connect(event) {
      chatPage.classList.remove("hidden");

      var socket = new SockJS("/websocket");
      stompClient = Stomp.over(socket);

      stompClient.connect({}, onConnected, onError);
 
}

function onConnected() {
  // Subscribe to the Public Topic
  stompClient.subscribe("/topic/public", onMessageReceived);

  // Tell your username to the server
  stompClient.send(
    "/app/chat.register",
    {},
    JSON.stringify({ sender: username, type: "JOIN" })
  );

  connectingElement.classList.add("hidden");
}

function onError(error) {
  connectingElement.textContent =
    "Could not connect to WebSocket! Please refresh the page and try again or contact your administrator.";
  connectingElement.style.color = "red";
}

function send(event) {
	event.preventDefault();
  var messageContent = messageInput.value.trim();
  const fileInput = document.querySelector("#fileInput");
   const file = fileInput ? fileInput.files[0] : null;

  if ((messageContent || file) && stompClient) {
    const chatMessage = {
      sender: username,
       content: messageContent || "",
      type: "CHAT",
    };
	
	if (file) {
	     const formData = new FormData();
	     formData.append("file", file);
	     formData.append("sender", username);
	     formData.append("message", messageContent);
	// Send the file via API
	      fetch("/api/uploadFile", {
	        method: "POST",
	        body: formData,
	      })
	        .then((response) => {
	          if (response.ok) {
	            response.json().then((fileData) => {
	              // Include the uploaded file's URL in the message
				  chatMessage.fileName = fileData.name;  // Map the file name
				  chatMessage.fileUrl = fileData.url;
	              sendMessage(chatMessage); // Send the message via WebSocket
	            });
	          } else {
	            console.error("Failed to upload file");
	          }
	        })
	        .catch((error) => {
	          console.error("Error uploading file:", error);
	        });
	    } else {
	      sendMessage(chatMessage); // Send the message via WebSocket without a file
	    }
    messageInput.value = "";
	if (fileInput) {
	     fileInput.value = ""; // Clear the file input
	   }
  }
}


function sendMessage(chatMessage) {
    stompClient.send("/app/chat.send", {}, JSON.stringify(chatMessage));
}
/**
 * Handles the received message and updates the chat interface accordingly.
 * param {Object} payload - The payload containing the message data.
 */
function onMessageReceived(payload) {
  var message = JSON.parse(payload.body);

  var messageElement = document.createElement("li");

  if (message.type === "JOIN") {
    messageElement.classList.add("event-message");
    message.content = message.sender + " joined!";
  } else if (message.type === "LEAVE") {
    messageElement.classList.add("event-message");
    message.content = message.sender + " left!";
  } else {
    messageElement.classList.add("chat-message");

    var avatarElement = document.createElement("i");
    var avatarText = document.createTextNode(message.sender[0]);
    avatarElement.appendChild(avatarText);
    avatarElement.style["background-color"] = getAvatarColor(message.sender);

    messageElement.appendChild(avatarElement);

    var usernameElement = document.createElement("span");
    var usernameText = document.createTextNode(message.sender);
    usernameElement.appendChild(usernameText);
    messageElement.appendChild(usernameElement);
    // * update
    usernameElement.style["color"] = getAvatarColor(message.sender);
    //* update end
  }

  var textElement = document.createElement("p");
  var messageText = document.createTextNode(message.content);
  textElement.appendChild(messageText);

  messageElement.appendChild(textElement);
  // * update
  if (message.sender === username) {
    // Add a class to float the message to the right
    messageElement.classList.add("own-message");
  } // * update end
  
  if (message.fileUrl) {
    const fileLink = document.createElement("a");
     fileLink.href = `/api/files/${message.fileName}`;  // Use the fileUrl to link the file
    fileLink.download = message.fileName;  // Set the download filename
    fileLink.textContent = `Download ${message.fileName}`;
    fileLink.target = "_blank";  // Open in a new tab if it's an external file
    messageElement.appendChild(fileLink);
  }
  
  
  messageArea.appendChild(messageElement);
  messageArea.scrollTop = messageArea.scrollHeight;
}

function getAvatarColor(messageSender) {
  var hash = 0;
  for (var i = 0; i < messageSender.length; i++) {
    hash = 31 * hash + messageSender.charCodeAt(i);
  }

  var index = Math.abs(hash % colors.length);
  return colors[index];
}


window.onload = function() {
    connect();  // Directly call the connect function
};
if(messageForm){
messageForm.addEventListener("submit", send, true);
}
