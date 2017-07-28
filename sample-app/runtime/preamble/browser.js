window.React = require("react");
window.reactCreateFragment = require('react-addons-create-fragment');
window.ReactDOM = require("react-dom");
window.ReactDOMServer = require("react-dom/server"); // Reagent depends on ReactDOMServer in browsers
window.Promise = require("bluebird");
window.CodeMirror = require("codemirror");
window.jwtDecode = require("jwt-decode");
window.Cookies = require("js-cookie");
window.NativeListener = require("react-native-listener");
window.Color = require("color");
window.deepdiff = require('deep-diff');
window.moment = require('moment');

require("codemirror/mode/clojure/clojure");
require("codemirror/mode/markdown/markdown");
require("codemirror/mode/jsx/jsx");
require("codemirror/addon/edit/closebrackets");
require("codemirror/addon/edit/matchbrackets");

require('codemirror/lib/codemirror.css');
require('bootstrap/dist/css/bootstrap.css');
//require('re-com/css/material-design-iconic-font.min.css');
require('re-com/css/re-com.css');
require('animate.css/animate.css');
require('browser.css');
require('hyperfiddle.css');
