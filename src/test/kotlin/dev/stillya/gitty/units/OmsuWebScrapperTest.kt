package dev.stillya.gitty.units

import dev.stillya.gitty.services.bot.omsu.OmsuWebScrapper
import org.jsoup.HttpStatusException
import org.junit.Test
import org.junit.jupiter.api.Assertions

class OmsuWebScrapperTest {
    private val omsuWebScrapper = OmsuWebScrapper()

    @Test
    fun `should return correct data`() {
        val result = omsuWebScrapper.getOmsuParticipants("7803") // no exception means that data is correct
        Assertions.assertEquals(
            "Факультет математики и информационных технологий\\Очная\\ФУНДАМЕНТАЛЬНЫЕ МАТЕМАТИКА И МЕХАНИКА\\Фундаментальная математика(ПЛАН)-ДСПЦ",
            result.second!!.nameOfSpeciality
        )
        Assertions.assertTrue(result.second!!.lastUpdateTime.matches(Regex("Данные на .*")))
        Assertions.assertEquals(
            "Контрольные цифры приема - 18 (основные конкурсные места и места в пределах целевой и особой квот)",
            result.second!!.numbers
        )
    }

    @Test(expected = Exception::class)
    fun `should failed for non exist id`() {
        omsuWebScrapper.getOmsuParticipants("0") // exception means that data is not correct
    }
}