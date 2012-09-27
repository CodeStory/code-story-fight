Browser = require 'zombie'
assert = require 'assert'

browser = new Browser()
browser.visit "http://localhost:8080/", ->
  assert.equal browser.text('title'), 'CodeStory'