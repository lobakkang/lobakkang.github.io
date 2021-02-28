package lobakkang.openlcd.tile;

import li.cil.oc.api.Network;
import li.cil.oc.api.machine.Arguments;
import li.cil.oc.api.machine.Callback;
import li.cil.oc.api.machine.Context;
import li.cil.oc.api.network.Visibility;
import li.cil.oc.api.prefab.TileEntityEnvironment;

public class TileLCD extends TileEntityEnvironment {
    public static final double EnergyCostPerTick = 0.5;
    protected boolean isEnabled = true;
    protected boolean hasEnergy;

    public TileLCD() {
        node = Network.newNode(this, Visibility.Network).withConnector().withComponent("LCD").create();
    }

    @Callback
    public Object[] isOn(Context context, Arguments args) {
        return new Object[]{isEnabled};
    }
}
