package lobakkang.openlcd.network;

import lobakkang.openlcd.tile.TileLCD;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class MessageHandlerOnServer implements IMessageHandler<MessageToServer, IMessage> {
    public IMessage onMessage(final MessageToServer message, MessageContext ctx) {
        System.out.println("A message is received at server side");
        if (ctx.side != Side.SERVER) {
            System.err.println("AirstrikeMessageToServer received on wrong side:" + ctx.side);
            return null;
        }
        if (!message.isMessageValid()) {
            System.err.println("AirstrikeMessageToServer was invalid" + message.toString());
            return null;
        }

        final EntityPlayerMP sendingPlayer = ctx.getServerHandler().player;
        if (sendingPlayer == null) {
            System.err.println("EntityPlayerMP was null when AirstrikeMessageToServer was received");
            return null;
        }
        final WorldServer playerWorldServer = sendingPlayer.getServerWorld();
        playerWorldServer.addScheduledTask(new Runnable() {
            public void run() {
                processMessage(message, sendingPlayer);
            }
        });

        return null;
    }

    void processMessage(MessageToServer message, EntityPlayerMP sendingPlayer) {
        MinecraftServer minecraftServer = sendingPlayer.getServer();
        NBTTagCompound tag = message.getNBTTagCompound();

        BlockPos pos = new BlockPos(tag.getInteger("x"), tag.getInteger("y"), tag.getInteger("z"));

        TileLCD tileLCD = (TileLCD) minecraftServer.getEntityWorld().getTileEntity(pos);
        tileLCD.is_gui_open = tag.getBoolean("is_gui_open");

        return;
    }
}