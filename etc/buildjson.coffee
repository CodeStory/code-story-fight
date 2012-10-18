#!/usr/bin/env coffee 

# prerequisites
# npm install -g underscore

_ = require('underscore')
http = require('http')
fs = require('fs')

#class Talk
#  constructor: (@speakers, @title, @fromTime, @toTime, @kind, @room) ->


class ScheduleRetriever
  constructor: (@eventId, @outputfile) ->
    @scheduleUri = "http://cfp.devoxx.com/rest/v1/events/#{@eventId}/schedule"

  writeToDisk: ->
    if outputFile?
      fs.writeFileSync(@outputfile, JSON.stringify(@talkData), 'utf8')
      console.log "written #{@outputfile}"
    else
      console.log JSON.stringify @talkData

  transform: ->
    @talkData = _.chain(@talkData)
    .filter((event) -> event.speaker?)
    .map((event) -> _.omit event, ['partnerSlot', 'code', 'speakerUri'])
    .sortBy((event) -> event.fromTime)
    .groupBy((event) -> event.fromTime[0..9])
    .map((events, day) -> {
      day : day,  #this sucks
      events : _.groupBy events, (event) -> event.fromTime[11..15]
    })
    .flatten()
    .value()


    # add days !
    # 2 get extra data
    # end of story
    @writeToDisk()

  getMainSchedule: ->
    http.get @scheduleUri, (response) =>
      scheduleData = ""
      response.on 'data', (data) ->
        scheduleData += data
      response.on 'end', =>
        @talkData = JSON.parse scheduleData
        @transform()

  retrieveSchedule: ->
    @getMainSchedule()

scheduleRetriever = new ScheduleRetriever(7).retrieveSchedule()

