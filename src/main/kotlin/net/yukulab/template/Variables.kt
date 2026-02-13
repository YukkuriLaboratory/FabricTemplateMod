package net.yukulab.template

import kotlinx.coroutines.CoroutineDispatcher
import net.minecraft.resources.Identifier
import kotlin.coroutines.CoroutineContext

internal const val MOD_ID = "yukulabtemplate"

internal const val MOD_NAME = "YukulabTemplate"

internal val modDispatcher: CoroutineContext
    get() = YukulabTemplate.modDispatcher

internal val serverDispatcher: CoroutineDispatcher
    get() = YukulabTemplate.serverDispatcher

internal fun id(path: String) = Identifier.fromNamespaceAndPath(MOD_ID, path)
