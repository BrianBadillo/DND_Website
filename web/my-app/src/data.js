export const mockData = {
    user: [
        { user_id: 1, name: "John", email: "john0@example" },
        { user_id: 8, name: "Smith", email: "smith1@example" },
        { user_id: 10, name: "Jane", email: "jane2@example" },
        { user_id: 12, name: "Doe", email: "doe3@example" },
        { user_id: 13, name: "Alice", email: "alice4@example" },
        { user_id: 14, name: "Bob", email: "bob5@example" },
        { user_id: 15, name: "Charlie", email: "charlie6@example" },
        { user_id: 16, name: "David", email: "david7@example" },
        { user_id: 17, name: "Eve", email: "eve8@example" },
        { user_id: 18, name: "Frank", email: "frank9@example" },
        { user_id: 19, name: "Grace", email: "grace10@example" },
        { user_id: 20, name: "Hannah", email: "hannah11@example" },
        { user_id: 21, name: "Ivy", email: "ivy12@example" },
        { user_id: 22, name: "Jack", email: "jack13@example" },
        { user_id: 23, name: "Kyle", email: "kyle14@example" }
    ],
    characters: [
        { char_id: 1, name: "Arthas", class: "Paladin", level: 10, user_id: 1, group_id: 1 },
        { char_id: 2, name: "Jaina", class: "Mage", level: 12, user_id: 1, group_id: 2 },
        { char_id: 3, name: "Thrall", class: "Shaman", level: 8, user_id: 1, group_id: 3 },
        { char_id: 4, name: "Sylvanas", class: "Hunter", level: 15, user_id: 8, group_id: 1 },
        { char_id: 5, name: "Vol'jin", class: "Druid", level: 9, user_id: 10, group_id: 2 },
        { char_id: 6, name: "Gul'dan", class: "Warlock", level: 11, user_id: 12, group_id: 3 },
        { char_id: 7, name: "Tyrande", class: "Priest", level: 7, user_id: 13, group_id: 1 },
        { char_id: 8, name: "Kael'thas", class: "Mage", level: 14, user_id: 14, group_id: 2 },
        { char_id: 9, name: "Anub'arak", class: "Death Knight", level: 6, user_id: 15, group_id: 3 },
        { char_id: 10, name: "Illidan", class: "Demon Hunter", level: 16, user_id: 16, group_id: 1 },
        { char_id: 11, name: "Rexxar", class: "Beastmaster", level: 10, user_id: 17, group_id: 2 },
        { char_id: 12, name: "Jaina", class: "Mage", level: 13, user_id: 18, group_id: 3 },
        { char_id: 13, name: "Grom", class: "Warrior", level: 8, user_id: 19, group_id: 1 },
        { char_id: 14, name: "Uther", class: "Paladin", level: 17, user_id: 20, group_id: 2 },
        { char_id: 15, name: "Kel'Thuzad", class: "Necromancer", level: 9, user_id: 21, group_id: 3 },
        { char_id: 16, name: "Ner'zhul", class: "Warlock", level: 7, user_id: 22, group_id: 1 },
        { char_id: 17, name: "Cairne", class: "Warrior", level: 11, user_id: 23, group_id: 2 }
    ],
    groups: [
        { group_id: 1, name: "The Fellowship", dm_id: 10 },
        { group_id: 2, name: "Knights of Valor", dm_id: 12 },
        { group_id: 3, name: "Legends of the Realm", dm_id: 8 }
    ],
    items: [
        { item_id: 1, item_name: "Adamantine Armor", item_description: "This suit of armor is reinforced with adamantine, one of the hardest substances in existence.", character_id: 1, is_shared: true, rarity: "Uncommon", type: "Armor" },
        { item_id: 2, item_name: "Apparatus of Kwalish", item_description: "A wondrous item capable of transforming into a submersible vehicle.", character_id: 2, is_shared: false, rarity: "Legendary", type: "Wondrous Item" },
        { item_id: 3, item_name: "Potion of Healing", item_description: "A basic healing potion that restores health.", character_id: 3, is_shared: true, rarity: "Common", type: "Potion" },
        { item_id: 4, item_name: "Cloak of Protection", item_description: "A cloak that grants protection from magical attacks.", character_id: 1, is_shared: true, rarity: "Rare", type: "Cloak" },
        { item_id: 5, item_name: "Ring of Invisibility", item_description: "A magical ring that grants invisibility.", character_id: 2, is_shared: false, rarity: "Very Rare", type: "Ring" },
        { item_id: 6, item_name: "Shadowblade", item_description: "A sword that deals extra damage in the dark.", character_id: 4, is_shared: true, rarity: "Rare", type: "Weapon" },
        { item_id: 7, item_name: "Gems of the Endless Void", item_description: "A set of gems that grant power over shadows.", character_id: 5, is_shared: false, rarity: "Legendary", type: "Gems" },
        { item_id: 8, item_name: "Healing Potion", item_description: "A potent healing potion that restores large amounts of health.", character_id: 6, is_shared: true, rarity: "Uncommon", type: "Potion" },
        { item_id: 9, item_name: "Staff of Arcane Might", item_description: "A staff that amplifies arcane spells.", character_id: 7, is_shared: false, rarity: "Rare", type: "Staff" },
        { item_id: 10, item_name: "Phoenix Feather", item_description: "A rare feather from a Phoenix, imbued with resurrection powers.", character_id: 8, is_shared: true, rarity: "Very Rare", type: "Artifact" },
        { item_id: 11, item_name: "Icebound Fortitude", item_description: "A cloak that grants temporary invulnerability.", character_id: 9, is_shared: false, rarity: "Epic", type: "Cloak" },
        { item_id: 12, item_name: "Essence of the Fallen", item_description: "A potion that restores both health and mana.", character_id: 10, is_shared: true, rarity: "Rare", type: "Potion" },
        { item_id: 13, item_name: "Ring of Power", item_description: "A ring that grants the wearer control over lesser creatures.", character_id: 11, is_shared: true, rarity: "Epic", type: "Ring" },
        { item_id: 14, item_name: "Celestial Shield", item_description: "A shield forged by the gods that blocks even the most powerful attacks.", character_id: 12, is_shared: false, rarity: "Legendary", type: "Shield" },
        { item_id: 15, item_name: "Blade of the Phoenix", item_description: "A sword that burns with the fury of a Phoenix.", character_id: 13, is_shared: true, rarity: "Epic", type: "Weapon" },
        { item_id: 16, item_name: "Wand of Illusion", item_description: "A wand that allows the caster to create illusions.", character_id: 14, is_shared: false, rarity: "Uncommon", type: "Wand" },
        { item_id: 17, item_name: "Crown of the Undying", item_description: "A crown that grants immortality for a limited time.", character_id: 15, is_shared: true, rarity: "Very Rare", type: "Crown" },
        { item_id: 18, item_name: "Boots of Speed", item_description: "Boots that greatly increase the wearer's speed.", character_id: 16, is_shared: false, rarity: "Uncommon", type: "Boots" },
        { item_id: 19, item_name: "Amulet of the Elements", item_description: "An amulet that grants control over the elements.", character_id: 17, is_shared: true, rarity: "Epic", type: "Amulet" },
        { item_id: 20, item_name: "Tome of Knowledge", item_description: "A book that enhances the knowledge of arcane magic.", character_id: 17, is_shared: false, rarity: "Legendary", type: "Tome" }
    ]
};