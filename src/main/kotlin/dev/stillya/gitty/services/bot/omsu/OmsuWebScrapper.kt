package dev.stillya.gitty.services.bot.omsu

import dev.stillya.gitty.dtos.OmsuParticipantDto
import dev.stillya.gitty.dtos.OmsuStatisticDto
import mu.KLogging
import org.jsoup.HttpStatusException
import org.jsoup.Jsoup
import org.springframework.stereotype.Service

@Service
class OmsuWebScrapper {

    companion object : KLogging() {
        const val url = "https://abit.omsu.ru/abitpage/skripty2021-1?new="
    }

    fun getOmsuParticipants(id: String): Pair<List<OmsuParticipantDto>, OmsuStatisticDto?> {
        try {
            val webPage = Jsoup.connect(url + id).get()

            val fields =
                webPage.getElementsByClass("field-item even")[0].children()

            val nameOfSpecialty = fields[1].text() + fields[2].text() + fields[3].text() + fields[4].text() + fields[5].text()

            val numbers = fields[7].text()

            val lastUpdate = fields[10].text()

            val table = webPage.getElementsByClass("stripy")[0]

            val rows = table.getElementsByTag("tr")

            val result = mutableListOf<OmsuParticipantDto>()

            // without first row
            for (i in 1 until rows.size) {
                val row = rows[i]
                val cells = row.getElementsByTag("td")

                result.add(
                    OmsuParticipantDto(
                        cells[2].text(),
                        cells[5].text(),
                        cells[6].text(),
                        cells[7].text(),
                        cells[8].text(),
                        cells[9].text(),
                        cells[10].text(),
                        cells[11].text(),
                        cells[12].text(),
                        cells[13].text(),
                    )
                )
            }
            return Pair(result, OmsuStatisticDto(nameOfSpecialty, numbers, lastUpdate))
        } catch (e: HttpStatusException) {
            logger.error { "Error while getting OMSU participants: ${e.statusCode}" }
            return Pair(listOf(), null)
        }
    }
}