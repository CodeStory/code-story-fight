http = require('http')
fs = require('fs')

outputfile = 'schedule-devoxx.json'
eventId = 7

class Talk
  constructor: (@speakers, @title) ->

talks = []

days = [
  {
  day: "Monday"
  date: "2012-11-12"
  talks: []
  },
  {
  day: "Tuesday"
  date: "2012-11-13"
  talks: []
  },
  {
  day: "Wednesday"
  date: "2012-11-14"
  talks: []
  },
  {
  day: "Thursday"
  date: "2012-11-15"
  talks: []
  },
  {
  day: "Friday"
  date: "2012-11-16"
  talks: []
  }
]

indexByDays = [
  "2012-11-12",
  "2012-11-13",
  "2012-11-14",
  "2012-11-15",
  "2012-11-16"
]

getPresentation = (talksToRemain) ->
  if talksToRemain.length == 0
    talks.map (currentTalk) ->
      index = currentTalk.fromTime[0..9]
      days[indexByDays.indexOf(index)].talks.push currentTalk
    fs.writeFileSync(outputfile, JSON.stringify days, 'utf8')
#    finalJson = JSON.stringify talks
#    fs.writeFileSync(outputfile, finalJson, 'utf8')
    console.log "written #{outputfile}"
    return
  currentTalk = talksToRemain[0]
  console.log currentTalk.presentationUri
  http.get currentTalk.presentationUri, (response) ->
    if response.statusCode == 200
      presentationData = ""
      response.on 'data', (data) ->
        presentationData += data
      response.on 'end', () ->
        presentationJsonData = JSON.parse presentationData
        currentTalk.presentation = presentationJsonData.summary
        currentTalk.tags = presentationJsonData.tags
        currentTalk.presentationUri = ""
        talks.push currentTalk unless currentTalk == null
    else
      console.log("/!\\ got a #{response.statusCode} for #{currentTalk.presentationUri}")
    getPresentation(talksToRemain[1..talksToRemain.size - 2])

http.get "http://cfp.devoxx.com/rest/v1/events/#{eventId}/schedule", (response)->
  scheduleData = ""
  response.on 'data', (data)->
    scheduleData += data
  response.on 'end', () ->
    talksWithPresentationUri = []
    jsonData = JSON.parse scheduleData
    for talkData in jsonData
      if talkData.speakers?
        Talk talk = new Talk talkData.speakers, talkData.title
        talk.fromTime = talkData.fromTime
        talk.toTime = talkData.toTime
        talk.kind = talkData.kind
        talk.room = talkData.room
        if talkData.presentationUri?
          talk.presentationUri = talkData.presentationUri
          talksWithPresentationUri.push talk
        else
          talks.push talk
    getPresentation(talksWithPresentationUri)
