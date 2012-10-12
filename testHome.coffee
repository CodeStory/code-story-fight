Browser = require 'zombie'
assert = require 'assert'

port = process.argv[2]

browser = new Browser()
browser.visit "http://localhost:#{port}/", ->
  assert.equal browser.text('title'), 'CodeStory - Fight for Devoxx'