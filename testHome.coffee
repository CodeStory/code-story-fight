Browser = require 'zombie'
expect = require 'expect.js'

port = process.argv[2]

browser = new Browser()
browser.visit "http://localhost:#{port}/", ->
  expect(browser.text 'title').to.be 'CodeStory - Fight for Devoxx'

  expect(browser.text '#session01 .title').to.be 'Scala m\'a tuer !'
  expect(browser.text '#session01 .speaker').to.be 'John Doe'
  expect(browser.text '#session01 .description').to.contain 'Lorem ipsum'
  expect(browser.text '#session01 .tags .tag').to.contain 'Scala'
  expect(browser.text '#session01 .tags .tag').to.contain 'Troll'

  expect(browser.text '#session03 .title').to.be 'Java m\'a tuer !'
  expect(browser.text '#session03 .speaker').to.be 'Jane Doe'
  expect(browser.text '#session03 .description').to.contain 'Lorem ipsum'
  expect(browser.text '#session03 .tags .tag').to.contain 'Java'