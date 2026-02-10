package net.yukulab.template

import me.shedaniel.autoconfig.ConfigData
import me.shedaniel.autoconfig.annotation.Config

@Config(name = YukulabTemplate.MOD_ID)
class ModConfig : ConfigData {
    var example: String = "example"
}
