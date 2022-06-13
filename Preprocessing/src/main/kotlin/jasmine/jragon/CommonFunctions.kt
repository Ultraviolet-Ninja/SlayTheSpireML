package jasmine.jragon

import org.json.JSONObject
import java.time.LocalDate

fun isGoodFile(data: JSONObject): Boolean {
    if (data.has("daily_mods"))
        return false

    val necessaryFields = listOf("is_trial", "is_daily", "chose_seed", "is_endless", "damage_taken",
        "event_choices", "card_choices", "relics_obtained", "campfire_choices",
        "items_purchased", "item_purchase_floors", "items_purged", "items_purged_floors",
        "character_chosen", "boss_relics", "floor_reached", "master_deck", "relics",
        "floor_reached", "circlet_count", "score", "player_experience", "build_version")

    for (field in necessaryFields) {
        if (!data.has(field))
            return false
    }

    for (field in necessaryFields.subList(0, 4)) {
        if (data.getBoolean(field))
            return false
    }

    if (!hasValidBuildNumber(data.getString("build_version"), data.getString("character_chosen")))
        return false

    val floorCount = data.getInt("floor_reached")
    if (floorCount < 4 || floorCount > 60)
        return false

    if (data.getInt("circlet_count") > 0)
        return false

    if (data.getInt("score") < 10)
        return false

    if (data.getInt("player_experience") < 100)
        return false

    val characterSet = GameCharacter.values()
        .map { c -> c.toString() }
        .toSet()

    if (!characterSet.contains(data.getString("character_chosen")))
        return false

    return true
}

private fun hasValidBuildNumber(dateStr: String, charChosen: String): Boolean {
    val date = LocalDate.parse(dateStr)

    return when (GameCharacter.valueOf(charChosen)) {
        GameCharacter.WATCHER -> date > LocalDate.parse("2020-01-15")
        else -> date > LocalDate.parse("2019-01-22")
    }
}