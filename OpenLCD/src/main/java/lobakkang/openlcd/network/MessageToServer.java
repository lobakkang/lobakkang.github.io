package lobakkang.openlcd.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class MessageToServer implements IMessage {
    public MessageToServer(NBTTagCompound tag) {
        this.tag = tag;
        messageIsValid = true;
    }

    public NBTTagCompound getNBTTagCompound() {
        return tag;
    }

    public boolean isMessageValid() {
        return messageIsValid;
    }

    public MessageToServer() {
        messageIsValid = false;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        try {
            tag = ByteBufUtils.readTag(buf);
        } catch (IndexOutOfBoundsException ioe) {
            System.err.println("Exception while reading MessagetoServer: " + ioe);
            return;
        }
        messageIsValid = true;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        if (!messageIsValid)
            return;
        ByteBufUtils.writeTag(buf, tag);
    }

    @Override
    public String toString() {
        return "MessageToServer=" + tag.toString();
    }

    private NBTTagCompound tag;
    private boolean messageIsValid;
}
