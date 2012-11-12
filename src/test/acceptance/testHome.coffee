should = require('chai').should()
Browser = require 'zombie'

port = 8080
port = process.env.PORT if process.env.PORT

home = "http://localhost:#{port}/"
planning = "#{home}planning.html"

newBrowser = ->
  new Browser({ "maxWait": 10000, "waitFor": 10000 })

#unstar to debug
#Browser.debug=true

describe 'Planning', ->
  it 'should show the title', (done) ->
    browser = newBrowser()
    browser.visit home, ->
      browser.text('title').should.equal 'CodeStory - Devoxx Planning'
      done()

  it 'should show toc', (done) ->
    browser = newBrowser()
    browser.visit planning, ->
      browser.text('.toc a').should.contain '09:30'
      done()

  it 'should show sessions', (done) ->
    browser = newBrowser()
    browser.visit planning, ->
      browser.text('#talk-759 h2').should.equal 'Android Development Code Lab (Hands-on Labs)'
      browser.text('#talk-759 .speaker').should.equal 'Nick Butcher, Richard Hyndman @BOF 1 Monday from 09:30 to 12:30'
      browser.text('#talk-759 p').should.contain 'Dive into some of the latest'
      browser.text('#talk-761 h2').should.equal 'Modular Architecture Today (University)'
      browser.text('#talk-761 .speaker').should.equal 'Kirk Knoernschild @Room 8 Monday from 09:30 to 12:30'
      browser.text('#talk-761 p').substring(0, 42).should.equal 'Modularity is coming to the Java platform!'
      done()

  it 'should redirect to authentication when user star', (done) ->
    browser = newBrowser()
    browser.visit planning, ->
      browser.clickLink '#talk-759 .star', ->
        browser.location.href.should.contain '/planning.html'
        browser.text('#auth a').should.equal 'Log Out'
        done()

  it 'should star while logged in', (done) ->
    browser = newBrowser()
    browser.visit planning, ->
      browser.text('#auth a').should.equal 'Log In'
      should.not.exist browser.cookies().get('userId')
      browser.query('#talk-759 .star').should.be.ok
      browser.query('#talk-760 .star').should.be.ok
      browser.query('#talk-761 .star').should.be.ok
      should.not.exist browser.query('#talk-759 .starred')
      should.not.exist browser.query('#talk-760 .starred')
      should.not.exist browser.query('#talk-761 .starred')

      browser.clickLink '#login', ->
        browser.text('#auth a').should.equal 'Log Out'
        browser.text('#screenName').should.equal '@arnold'
        browser.cookies().get('userId').should.equal '42'
        browser.cookies().get('screenName').should.equal 'arnold'
        browser.query('#talk-759 .star').should.be.ok
        should.not.exist browser.query('#talk-759 .starred')
        browser.clickLink '#talk-759 .star', ->
          browser.query('#talk-759 .starred').should.be.ok
          browser.clickLink '#talk-759 .star', ->
            browser.query('#talk-759 .star').should.be.ok
            should.not.exist browser.query '#talk-759 .starred'
            browser.clickLink '#logout', ->
              should.not.exist browser.cookies().get('userId')
              browser.text('#auth a').should.equal 'Log In'
              done()

  it 'Should filter with Url', (done) ->
    browser = newBrowser()
    browser.visit "#{planning}?q=foo", ->
      browser.query('#search_box').value.should.equal 'foo'
      done()