package com.bleudev.nlf2.client.sys.actor

import com.bleudev.nlf2.client.sys.actor.chars.*
import com.bleudev.nlf2.client.sys.actor.chars.res.*

object ActorCharEntrypoint {
    internal val chars: HashMap<String, ActorChar> = HashMap()

    fun getAllCharsNames(): List<String> = chars.keys.toList()

    fun initialize() {
        chars.clear()
        // Vessel like
        chars["rouge"] = VesselLikeChar("rouge")
        chars["rouge_battle"] = RougeBattleChar()
        chars["vert"] = VesselLikeChar("vert")
        chars["vessel"] = VesselLikeChar(null)
        // Only eyes
        chars["rouge_eyes"] = OnlyEyesLikeChar("rouge")
        chars["vert_eyes"] = OnlyEyesLikeChar("vert")
        // Mask
        chars["mask_bleugame"] = PlayerMaskChar("bleugame")
        chars["mask_crocko"] = PlayerMaskChar("crockomd")
        chars["mask_waflla"] = PlayerMaskChar("waflla_kek")
        chars["mask_sam"] = PlayerMaskChar("samivarence")
        chars["mask_milkich"] = PlayerMaskChar("milkich22397")
        chars["mask_boberobo"] = PlayerMaskChar("boberobo7")
        chars["mask_ync"] = PlayerMaskChar("ync27")
        // Visible only for bleugame
        chars["rouge_eyes_bleugame"] = RougeEyesBleugame()
        // Masks invisible for target
        chars["copy_bleugame"] = InvisibleForOnePlayerMask("bleugame")
        chars["copy_crocko"] = InvisibleForOnePlayerMask("crockomd")
        chars["copy_sam"] = InvisibleForOnePlayerMask("samivarence")
        chars["copy_milkich"] = InvisibleForOnePlayerMask("milkich22397")

        // Other
        chars["inv"] = InvChar()
        chars["ghost"] = GhostChar()
        chars["event101"] = Event101Char()
        chars["unknown"] = UnknownChar()
        chars["rip_sanya"] = RipSanyaChar()
    }
}