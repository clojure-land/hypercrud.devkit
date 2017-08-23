global.Color = require("./node_modules/color");
global.React = require("./node_modules/react");
global.reactCreateFragment = require('react-addons-create-fragment');
global.ReactDOM = require("./node_modules/react-dom");
global.ReactDOMServer = require("./node_modules/react-dom/server");
global.Promise = require("./node_modules/bluebird");

require('./main');
var args = process.argv.slice(2);
sample_app.main.main.apply(null, args);
