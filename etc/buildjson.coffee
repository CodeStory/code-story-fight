#!/usr/bin/env coffee 

_ = require('underscore')
http = require('http')
fs = require('fs')

class ScheduleRetriever
  constructor: (@eventId, @outputfile) ->
    @scheduleUri = "http://cfp.devoxx.com/rest/v1/events/#{@eventId}/schedule"

  transform: ->
    @talkData =
      _.chain(@talkData)
      .filter((talk) -> talk.speaker?)
      .map((talk) -> _.omit(talk, ['partnerSlot', 'code', 'speakerUri']))
      .sortBy((talk) -> talk.fromTime)
      .groupBy((talk) -> talk.fromTime[0..9])
      .map((talks, day) ->
        day: day,
        slots: _.chain(talks).groupBy((talk) -> talk.fromTime[11..15]).map((talks, slot) ->
          slot: slot,
          talks: talks
        ).value()
      )
      .value()

    console.log JSON.stringify { days: @talkData }

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
