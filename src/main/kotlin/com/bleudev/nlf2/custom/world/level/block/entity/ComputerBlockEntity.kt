package com.bleudev.nlf2.custom.world.level.block.entity

import com.bleudev.nine_lifes.util.sendPacket
import com.bleudev.nlf2.client.sys.util.ComputerTextBoxSequence
import com.bleudev.nlf2.custom.Nlf2BlockEntities
import com.bleudev.nlf2.custom.packet.payload.ComputerMessageSync
import com.bleudev.nlf2.custom.world.level.block.ComputerBlock
import com.bleudev.nlf2.custom.world.level.block.ComputerBlock.Companion.STATE
import com.bleudev.nlf2.sys.util.TextColorConstants
import net.minecraft.core.BlockPos
import net.minecraft.network.chat.Component
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.InteractionResult
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.storage.ValueInput
import net.minecraft.world.level.storage.ValueOutput

class ComputerBlockEntity(worldPosition: BlockPos, blockState: BlockState) : BlockEntity(
    Nlf2BlockEntities.COMPUTER, worldPosition, blockState
) {
    private var usedTimes: Int = 0
    private var messageTick: Int = 0

    override fun loadAdditional(input: ValueInput) {
        this.usedTimes = input.getIntOr("usedTimes", 0)
        this.messageTick = input.getIntOr("messageTick", 0)
    }

    override fun saveAdditional(output: ValueOutput) {
        output.putInt("usedTimes", this.usedTimes)
        output.putInt("messageTick", this.messageTick)
    }

    fun tick() {
        (level as? ServerLevel)?.players()?.forEach {
            it.sendPacket(ComputerMessageSync(blockPos, messageLines))
        }
        messageTick++
    }

    fun use(state: BlockState, level: Level, pos: BlockPos): InteractionResult {
        if (level.isClientSide) return InteractionResult.FAIL
        val maxCount = 9
        usedTimes++
        messageTick = 0
        level.setBlockAndUpdate(pos, state.setValue(STATE, when (usedTimes % maxCount) {
            // Start
            1 -> ComputerBlock.State.RED
            // Nooo!!!! This can't be!! I almost... I just wanted to be... strong... And after your success... I... became jealous... Why did you get all this? Why you, why not me??...
            2 -> ComputerBlock.State.RED_SAD
            // No, no, no... No need to apologize. The only one who should do this is me... Me...
            3 -> ComputerBlock.State.RED
            // a jealous vessel without any humanity, just wanted to get it by force??! I... almost ruined your entire career! I'm stupid!
            4 -> ComputerBlock.State.RED_SAD
            // Perhaps... But... before we say goodbye... Can I ask you a favor?.. Can you make my dream come true, can you unite with me?...
            5 -> ComputerBlock.State.RED
            // Of course, of course... You have a real human soul... Why would you need a dumb vessel?
            6 -> ComputerBlock.State.RED_SAD
            // Really???
            7 -> ComputerBlock.State.RED_SMILE
            // You're the best person I've ever met!
            8 -> ComputerBlock.State.RED_STAR
            else -> ComputerBlock.State.EMPTY
        }))
        return InteractionResult.PASS
    }

    /**
     * Use only on server
     * */
    private val messageLines: List<Component> get() {
        val curr = ComputerTextBoxSequence()
        curr.tick(messageTick)
        curr.speed(3)
        curr.color(TextColorConstants.ROUGE)

        fun add(s: String, w: Int = 40) {
            curr.add(s, w)
        }
        val nlTime = 5 // It's NL Time!???? (Nine Lifes??) - no, it's new line(
        val skipTime = 9999
        fun autoSkip(w: Int = 40) {
            if (messageTick >= curr.start - skipTime + w) {
                level?.let {
                    if (!it.isClientSide) {
                        use(it.getBlockState(blockPos), it, blockPos) // Simulate use to auto skip if we can
                    }
                }
            }
        }

        when (usedTimes % 9) {
            2 -> {
                add("nooo!!!! this cannot be!!")
                add("i almost... i just wanted", nlTime)
                add("to be... strong...")
                add("and after your success...", nlTime)
                add("i... became jealous...")
                add("why did you get all this?", nlTime)
                add("why you, why not me??...")
            }
            3 -> {
                add("No, no, no...", nlTime)
                add("No need to apologize.")
                add("The only one who should", nlTime)
                add("do this is me...", 20)
                add("me...", skipTime)
                autoSkip()
            }
            4 -> {
                add("a jealous vessel without", nlTime)
                add("any humanity...")
                add("just wanted to get it", nlTime)
                add("by force??!")
                add("i... almost ruined your", nlTime)
                add("entire career!", 20)
                add("i am stupid!")
            }
            5 -> {
                add("Perhaps... But...", nlTime)
                add("before we say goodbye...")
                add("Can I ask you a favor?..", nlTime)
                add("Can you make my dream come true?...")
                add("Can you unite with me?...", skipTime)
                autoSkip()
            }
            6 -> {
                add("of course, of course...", nlTime)
                add("you have a real human soul...", 20)
                add("why would you need a dumb vessel?")
            }
            7 -> {
                add("REALLY???", skipTime)
                autoSkip()
            }
            8 -> {
                add("YOU ARE THE BEST PERSON", nlTime)
                add("I HAVE EVER MET!", skipTime) // But do not skip!
            }
        }
        return curr.get()
    }

    companion object {
        fun tick(level: Level, pos: BlockPos, state: BlockState, entity: ComputerBlockEntity) {
            entity.tick()
        }
    }
}