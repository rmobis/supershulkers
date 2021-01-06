# Supershulkers

Gives shulker boxes super powers by adding three new exclusive enchantments: Enlarge, Restock and Vacuum.

**This mod is still largely experimental and many and big changes may still happen.** It's also my first mod. =D

## Enchantments

### Enlarge

Increases the number of slots in the shulker box. Each level adds an extra row (9 slots), up to 6 rows (54 slots).

### Restock

Whenever the last item on a stack is "consumed" (either by being used, placed, dropped or broken), that slot is filled with the first stack of the same item inside the shulker box. Also works for non-stackable items. [(video "demo")](https://youtu.be/sEQPMNN5UGk?t=1273)

### Vacuum

Items picked up will go directly into any shulker box that contains an incomplete stack of the same item. Only works with stackable items. [(video "demo")](https://youtu.be/sEQPMNN5UGk?t=461)


## Nice "Extra" Features

- Both vanilla and [ShulkerBoxTooltip](https://github.com/MisterPeModder/ShulkerBoxTooltip) tooltips work flawlessly. 


## TODO List

- [ ] verify anvils + books functionality
- [ ] add enchantment glint (hand, slot and block)
- [ ] balance enchantment rarity/required level
- [ ] pick a better name (super boxes? shulperboxes?)
- [ ] allow for `pickup_method` configuration through ModMenu
- [ ] allow for default restock method configuration through ModMenu
- [ ] implement vacuum/restock functionality "natively"
- [ ] drop overflown items when removing enlarge enchantment
- [ ] fix pick block in creative not preserving enchantments
- [ ] think of more "minecrafty" names for enchantments
- [ ] get a damn logo


## Inspiration and Credits

Most of the "creative content" of this mod was heavily inspired by xisumavoid's video [Minecraft : Enchanted Shulker Boxes (Inventory Update Suggestions)](https://youtu.be/FMu8T8KriQY).

Most of the functionality for the restock and vacuum enchantments are implemented through a ([modified](https://github.com/rmobis/supershulkers/blob/master/src/main/resources/assets/supershulkers/scripts/supershulkers.sc)) [Scarpet script](https://github.com/gnembon/scarpet/blob/master/programs/survival/shulkerboxes.sc) running inside [Fabric Carpet](https://github.com/gnembon/fabric-carpet), both made by the legend @gnembon. He goes into extensive inner working details in his video [Minecraft 1.16.3: Automatic Shulkerboxes - Vacuum and Restock [scarpet app]](https://youtu.be/sEQPMNN5UGk). We use the `restock same` strategy for restocking.

A lot of the boilerplate and functionality for the enlarge enchantment was also based on the very similar [Better Shulkers](https://github.com/arxenix/better-shulkers) mod by @arxenix. 

I'm grateful for being able to expand upon their work. Please note, however, that none of these people are affiliated with this project nor have endorsed it.
