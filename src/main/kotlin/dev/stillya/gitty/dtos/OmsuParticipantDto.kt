package dev.stillya.gitty.dtos

data class OmsuParticipantDto(
    val code: String,
    val isAgree: String,
    val firstSubject: String,
    val firstResult: String,
    val secondSubject: String,
    val secondResult: String,
    val thirdSubject: String,
    val thirdResult: String,
    val sumOfResult: String,
    val indivResult: String
)

data class OmsuStatisticDto(
    val nameOfSpeciality: String,
    val numbers: String,
    val lastUpdateTime: String
)

fun makePrettyTableForTelegram(participants: Pair<List<OmsuParticipantDto>, OmsuStatisticDto?>): String {
    val table = StringBuilder()
    table.appendLine("<pre>")
    table.appendLine("<b>Stats</b>")
    table.appendLine("<b>Last update:</b> ${participants.second?.lastUpdateTime}")
    table.appendLine("<b>Speciality:</b> ${participants.second?.nameOfSpeciality}")
    table.appendLine("<b>Numbers:</b> ${participants.second?.numbers}")
    table.appendLine("| Code | IsAgree | FirstSubject | FirstResult | SecondSubject | SecondResult | ThirdSubject | ThirdResult | SumOfResult | IndivResult |")
    table.appendLine("|:----:|:------:|:------------|:------------|:------------|:------------|:------------|:------------|:------------|:------------|")
    participants.first.forEach {
        table.appendLine("| ${it.code} | ${it.isAgree} | ${it.firstSubject} | ${it.firstResult} | ${it.secondSubject} | ${it.secondResult} | ${it.thirdSubject} | ${it.thirdResult} | ${it.sumOfResult} | ${it.indivResult} |")
    }
    table.appendLine("</pre>")
    return table.toString()
}
