package net.rudahee.metallics_arts.modules.items.metalminds.bands;


import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.rudahee.metallics_arts.modules.data_player.InvestedCapability;
import net.rudahee.metallics_arts.setup.enums.extras.MetalsNBTData;
import net.rudahee.metallics_arts.setup.network.ModNetwork;
import top.theillusivec4.curios.api.SlotContext;

public class BandChromiumNicrosil extends BandMindAbstract {

    public BandChromiumNicrosil(Item.Properties properties){
        super(properties, MetalsNBTData.CHROMIUM,MetalsNBTData.NICROSIL,MetalsNBTData.CHROMIUM.getMaxReserveBand(),MetalsNBTData.NICROSIL.getMaxReserveBand());
    }

    private boolean nicConsume = false;

    @Override
    public void curioTick(SlotContext slotContext, ItemStack stack) {
        LivingEntity livingEntity = slotContext.entity();

        CompoundTag nbtLocal = stack.getTag();

        if (livingEntity.level instanceof ServerLevel) {
            if (livingEntity instanceof Player) {
                Player player = (Player) livingEntity;
                player.getCapability(InvestedCapability.PLAYER_CAP).ifPresent(data -> {

                    if (data.isDecanting(MetalsNBTData.ALUMINUM)||data.isStoring(MetalsNBTData.ALUMINUM)){
                        stack.getTag().putString("key",changeOwner(player,stack.getTag(),false));
                    }

                    if (data.isDecanting(getMetals(0))) {
                        if (stack.getTag().getInt(getMetals(0).getNameLower()+"_feruchemic_reserve")>0) {

                            if (data.isDecanting(MetalsNBTData.NICROSIL)){
                                if (!nicConsume){
                                    nbtLocal.putInt(getMetals(0).getNameLower()+"_feruchemic_reserve",(stack.getTag().getInt(getMetals(0).getNameLower()+"_feruchemic_reserve")-1));
                                    stack.setTag(nbtLocal);
                                }

                            } else {
                                //las dos lineas de abajo van sin el nicrosil
                                nbtLocal.putInt(getMetals(0).getNameLower()+"_feruchemic_reserve",(stack.getTag().getInt(getMetals(0).getNameLower()+"_feruchemic_reserve")-1));
                                stack.setTag(nbtLocal);
                            }



                        } else {
                            stack.getTag().putString("key",changeOwner(player,stack.getTag(),false));
                            data.setDecanting(getMetals(0),false);
                        }
                    } else if (data.isStoring(getMetals(0))) {
                        if (stack.getTag().getInt(getMetals(0).getNameLower()+"_feruchemic_reserve") < stack.getTag().getInt(getMetals(0).getNameLower()+"_feruchemic_max_capacity")) {
                            if (data.isStoring(MetalsNBTData.NICROSIL)) {

                                if (!nicConsume){
                                    stack.getTag().putString("key",changeOwner(player,stack.getTag(),true));
                                    nbtLocal.putInt(getMetals(0).getNameLower()+"_feruchemic_reserve",(stack.getTag().getInt(getMetals(0).getNameLower()+"_feruchemic_reserve")+1));
                                    stack.setTag(nbtLocal);
                                }


                            } else {
                                //estas 3 lineas ban sin la logica del nocrosil
                                stack.getTag().putString("key",changeOwner(player,stack.getTag(),true));
                                nbtLocal.putInt(getMetals(0).getNameLower()+"_feruchemic_reserve",(stack.getTag().getInt(getMetals(0).getNameLower()+"_feruchemic_reserve")+1));
                                stack.setTag(nbtLocal);
                            }



                        } else {
                            data.setStoring(getMetals(0),false);
                        }
                    }
                    ///////////NICROSIL/////////////
                    if (data.isDecanting(getMetals(1))) {
                        if (stack.getTag().getInt(getMetals(1).getNameLower()+"_feruchemic_reserve")>0) {
                            if (data.cantMetalsDecanting() != 0){
                                if (!nicConsume){
                                    nbtLocal.putInt(getMetals(1).getNameLower()+"_feruchemic_reserve",(stack.getTag().getInt(getMetals(1).getNameLower()+"_feruchemic_reserve")-data.cantMetalsDecanting()));
                                    stack.setTag(nbtLocal);
                                }
                                nicConsume = !nicConsume;
                            }
                        } else {
                            stack.getTag().putString("key",changeOwner(player,stack.getTag(),false));
                            data.setDecanting(getMetals(1),false);
                        }
                    } else if (data.isStoring(getMetals(1))) {
                        if (stack.getTag().getInt(getMetals(1).getNameLower()+"_feruchemic_reserve") < stack.getTag().getInt(getMetals(1).getNameLower()+"_feruchemic_max_capacity")) {
                            if (data.cantMetalsStoring()>0){
                                if (!nicConsume){
                                    stack.getTag().putString("key",changeOwner(player,stack.getTag(),true));

                                    if (data.isStoring(MetalsNBTData.BRASS)){
                                        if (!player.isOnFire()){
                                            nbtLocal.putInt(getMetals(1).getNameLower()+"_feruchemic_reserve",(stack.getTag().getInt(getMetals(1).getNameLower()+"_feruchemic_reserve")+(data.cantMetalsStoring()-1)));
                                        } else {
                                            nbtLocal.putInt(getMetals(1).getNameLower()+"_feruchemic_reserve",(stack.getTag().getInt(getMetals(1).getNameLower()+"_feruchemic_reserve")+data.cantMetalsStoring()));
                                        }
                                    } else {
                                        nbtLocal.putInt(getMetals(1).getNameLower()+"_feruchemic_reserve",(stack.getTag().getInt(getMetals(1).getNameLower()+"_feruchemic_reserve")+data.cantMetalsStoring()));
                                    }

                                    stack.setTag(nbtLocal);
                                }
                                nicConsume = !nicConsume;

                            }
                        } else {
                            data.setStoring(getMetals(1),false);
                        }
                    }
                    ModNetwork.sync(data, player);
                });
            }
        }
        super.curioTick(slotContext, stack);
    }

}