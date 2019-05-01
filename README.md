# t-remote

<h1 align=center>
<img src="logo/horizontal.png" width=40%>
</h1>

<h2> What is this app? </h2>
T-remote is a very simple app for Android that works as a remote control for presentations. Sometimes you have to switch between slides on a slideshow and it is not convenient to keep pressing the keyboard arrows, or a physical remote controller is not available. T-remote tackles this problem with a very simple interface, compatible with almost every smartphone out there and free of charge.

<h2> How does it work? </h2>
T-remote works by exchanging messages between a server (the PC where the presentation is) and a mobile phone. It requires the phone and the PC to be on the same network.

First of all, you have to start the server in the PC. In order to do this, just open the java executable (Tserver.jar). Then, the server's IP and the PIN will appear on screen. In mobile phone, open T-remote app and write down the IP of the server and the PIN, and click on Connect. And that's all! In the next screen, you can press volume up key to go to the previous slide and volume down key to go to the next slide (having the focus on the presentation).

<h2> Is it safe? </h2>
Yes, the app is safe. Since the code is free for everyone to look into, any programmer can see that it has no backdoors, viruses, or anything of concern. The worst you can get is a force close on the app, which can happen since it is still on alpha. Future versions will have bugs corrected. Stay tuned!

<h2> Technical breakdown </h2>
The app is fairly simple. First of all, the Tserver app (PC) opens a UDP server on port 2050 (you can change that in code). Then it listens indefinitely for a message with the format XXXX#0001, where XXXX is the PIN given to the user. Any other messages will be discarded by the server, as well as incorrect PINs.<br>
If the wrong PIN is introduced three times, then the server will restart and another PIN will be offered to the user.<br>
If the right PIN is introduced, the server starts listening for messages with the format XXXX#YYYY, where XXXX is the PIN again (included in every message to stop any attempt of ID stealing) and YYYY is the code for the key pressed (0002 for left, 0003 for right and 0004 for exit). According to this, the server will simulate the key presses on the computer.

<h2> Contributors </h2>
<a href="https://github.com/zularizal"> Zularizal </a>, made the app logo.

<h2> License </h2>
This work is under the GNU GPL v3.0 license, which means that anyone can modify and distribute it freely, as long as it is distributed under the same or similar license. Also, if you distribute it, don't forget to mention the author and the original contributors.
