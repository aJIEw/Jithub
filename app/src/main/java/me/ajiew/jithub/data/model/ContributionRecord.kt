package me.ajiew.jithub.data.model

/**
 * User contributions based on commits
 *
 * @param index the index of the contribution record
 * @param date display the date of the contribution record
 * @param number how many contribution in this day
 */
data class ContributionRecord(val index: Int, val date: String, var number: Int)