var appIP = require('ip').address();
var appPort = 4201;
var appName = "VALOORES";
var appSlogant = "Information has never been this intelligent" + " -> " + "Angular Server IP: " + appIP+":"+appPort;
var appPlatform = process.platform;
var command = "";

// Showing application banner
const showBanner = require('node-banner');
(async() => {
    await showBanner(appName, appSlogant);
})();

if(appPlatform.indexOf("win") != -1) {
    // Process to run if on windows
    // Process execution command application startup
    command = "node ./node_modules/@angular/cli/bin/ng serve --host " + appIP + " --port " + appPort;
    var exec_process = require("child_process").exec(command);
    exec_process.stdout.pipe(process.stdout)
    exec_process.on('exit', function() {
        process.exit()
    });
} else {
    // Process to run if on linux
    // -----------------------------
    // Steps to be performed on linux server for first time running the application before running 'npm start'
    // Run the following commands
    // export NODE_OPTIONS=--max_old_space_size=4096
    // npm run build
    // -----------------------------
    command = "nohup ng serve --host " + appIP + " --port " + appPort;
    var exec_process = require("child_process").exec(command);
    exec_process.stdout.pipe(process.stdout)
    exec_process.on('exit', function() {
        process.exit()
    });
}