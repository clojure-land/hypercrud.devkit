var webpack = require('webpack');
var path = require('path');
var ExtractTextPlugin = require("extract-text-webpack-plugin");

var prod = process.env.NODE_ENV === 'production';

module.exports = {
  devtool: prod ? null : 'inline-source-map',
  entry: {
    browser: './preamble/browser'
  },

  output: {
    filename: 'preamble.js',
    path: 'generated-resources-browser'
  },

  resolve: {
    extensions: ['', '.js'],
    root: [ path.resolve('./preamble') ],
    modulesDirectories: ['node_modules']
  },
  plugins: [
    new webpack.NoErrorsPlugin(),
    new ExtractTextPlugin('styles.css', { allChunks: true }),
    prod ? new webpack.optimize.DedupePlugin() : null,
    prod ? new webpack.optimize.UglifyJsPlugin() : null
  ],

  module: {
    loaders: [
      { test: /\.(woff2?|svg)$/, loader: 'url?limit=10000' },
      { test: /\.(ttf|eot)$/, loader: 'file' },
      { test: /\.(jpe?g|png|gif|svg|ico)$/, loader: 'url?limit=10000' },
      { test: /\.css$/, loader: ExtractTextPlugin.extract("style-loader", "css-loader") }
    ]
  }
};
