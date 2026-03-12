SET FOREIGN_KEY_CHECKS=0;

DELETE FROM boards WHERE type='trade';

-- ----------------------------
-- Table structure for `accounts`
-- ----------------------------
DROP TABLE IF EXISTS `accounts`;
CREATE TABLE `accounts` (
  `uid` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `id` varchar(20) NOT NULL DEFAULT '',
  `pw` varchar(255) NOT NULL DEFAULT '',
  `old_pw` varchar(255) NOT NULL DEFAULT '',
  `status` tinyint(1) unsigned NOT NULL DEFAULT '0',
  `time` int(10) NOT NULL DEFAULT '259200',
  `register_date` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' ON UPDATE CURRENT_TIMESTAMP,
  `logins_date` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  `block_date` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  `last_ip` varchar(20) NOT NULL DEFAULT '0',
  `email` varchar(255) NOT NULL DEFAULT '',
  `vote` varchar(255) NOT NULL DEFAULT '',
  `notice_uid` int(10) unsigned NOT NULL DEFAULT '0',
  `member` enum('true','false') NOT NULL DEFAULT 'false',
  `giran_dungeon_time` int(10) NOT NULL DEFAULT '0',
  `info_name` varchar(255) NOT NULL DEFAULT '',
  `info_phone_num` varchar(255) NOT NULL DEFAULT '',
  `info_bank_name` varchar(255) NOT NULL DEFAULT '',
  `info_bank_num` varchar(255) NOT NULL DEFAULT '',
  `giran_dungeon_count` int(10) NOT NULL DEFAULT '0',
  `자동사냥_이용시간` int(10) NOT NULL DEFAULT '0',
  `레벨달성체크` int(10) NOT NULL DEFAULT '0',
  `auto_count` int(10) NOT NULL DEFAULT '0',
  `daycount` int(10) NOT NULL DEFAULT '0',
  `daycheck` int(10) NOT NULL DEFAULT '0',
  `daytime` int(10) NOT NULL DEFAULT '0',
  PRIMARY KEY (`uid`),
  KEY `id` (`id`),
  KEY `pw` (`pw`),
  KEY `status` (`status`)
) ENGINE=MyISAM AUTO_INCREMENT=19 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of accounts
-- ----------------------------

-- ----------------------------
-- Table structure for `auto_clan_list`
-- ----------------------------
DROP TABLE IF EXISTS `auto_clan_list`;
CREATE TABLE `auto_clan_list` (
  `clan_id` int(10) NOT NULL DEFAULT '0',
  `clan_name` varchar(255) NOT NULL DEFAULT '',
  `gfx` int(10) NOT NULL DEFAULT '0',
  `가입레벨` int(10) NOT NULL DEFAULT '0',
  `군주` tinyint(3) NOT NULL DEFAULT '0',
  `기사` tinyint(3) NOT NULL DEFAULT '0',
  `요정` tinyint(3) NOT NULL DEFAULT '0',
  `마법사` tinyint(3) NOT NULL DEFAULT '0',
  `다크엘프` tinyint(3) NOT NULL DEFAULT '0',
  `x` int(10) NOT NULL DEFAULT '0',
  `y` int(10) NOT NULL DEFAULT '0',
  `map` int(10) NOT NULL DEFAULT '0',
  `heading` int(10) NOT NULL DEFAULT '0',
  PRIMARY KEY (`clan_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of auto_clan_list
-- ----------------------------

-- ----------------------------
-- Table structure for `auto_fish_list`
-- ----------------------------
DROP TABLE IF EXISTS `auto_fish_list`;
CREATE TABLE `auto_fish_list` (
  `account_uid` int(10) NOT NULL DEFAULT '0',
  `pc_objId` int(10) NOT NULL DEFAULT '0',
  `pc_name` varchar(20) NOT NULL DEFAULT '',
  `loc_x` int(5) NOT NULL DEFAULT '0',
  `loc_y` int(5) NOT NULL DEFAULT '0',
  `loc_map` int(10) NOT NULL DEFAULT '0',
  `heading` tinyint(2) NOT NULL DEFAULT '0',
  `gfx` int(10) NOT NULL DEFAULT '0',
  `gfx_mode` tinyint(3) NOT NULL DEFAULT '0',
  `fish_time` int(10) NOT NULL DEFAULT '0',
  `coin_count` bigint(20) NOT NULL DEFAULT '0',
  `rice_count` bigint(20) NOT NULL DEFAULT '0',
  PRIMARY KEY (`account_uid`,`pc_objId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of auto_fish_list
-- ----------------------------

-- ----------------------------
-- Table structure for `characters`
-- ----------------------------
DROP TABLE IF EXISTS `characters`;
CREATE TABLE `characters` (
  `name` varchar(20) NOT NULL DEFAULT '',
  `account` varchar(20) NOT NULL DEFAULT '',
  `account_uid` int(10) unsigned NOT NULL DEFAULT '0',
  `objID` int(10) unsigned NOT NULL,
  `level` int(10) unsigned NOT NULL DEFAULT '1',
  `nowHP` int(10) unsigned NOT NULL DEFAULT '0',
  `maxHP` int(10) unsigned NOT NULL DEFAULT '0',
  `nowMP` int(10) unsigned NOT NULL DEFAULT '0',
  `maxMP` int(10) unsigned NOT NULL DEFAULT '0',
  `ac` tinyint(3) unsigned NOT NULL DEFAULT '0',
  `str` tinyint(3) unsigned NOT NULL DEFAULT '0',
  `dex` tinyint(3) unsigned NOT NULL DEFAULT '0',
  `con` tinyint(3) unsigned NOT NULL DEFAULT '0',
  `wis` tinyint(3) unsigned NOT NULL DEFAULT '0',
  `inter` tinyint(3) unsigned NOT NULL DEFAULT '0',
  `cha` tinyint(3) unsigned NOT NULL DEFAULT '0',
  `sex` tinyint(3) unsigned NOT NULL DEFAULT '0',
  `class` tinyint(3) unsigned NOT NULL DEFAULT '0',
  `exp` double(20,0) NOT NULL DEFAULT '0',
  `lost_exp` double(20,0) NOT NULL DEFAULT '0',
  `locX` smallint(10) unsigned NOT NULL DEFAULT '32576',
  `locY` smallint(10) unsigned NOT NULL DEFAULT '32926',
  `locMAP` mediumint(10) unsigned NOT NULL DEFAULT '0',
  `locHeading` tinyint(3) NOT NULL DEFAULT '0',
  `title` varchar(20) NOT NULL DEFAULT '',
  `food` tinyint(3) unsigned NOT NULL DEFAULT '5',
  `gfx` int(10) unsigned NOT NULL DEFAULT '0',
  `gfxMode` int(10) unsigned NOT NULL DEFAULT '0',
  `lawful` mediumint(10) unsigned NOT NULL DEFAULT '65536',
  `clanID` mediumint(10) unsigned NOT NULL DEFAULT '0',
  `clanNAME` varchar(20) NOT NULL DEFAULT '',
  `pkcount` int(10) unsigned NOT NULL DEFAULT '0',
  `pkTime` datetime NOT NULL DEFAULT '0000-00-00 00:00:00',
  `global_chating` tinyint(3) NOT NULL DEFAULT '1',
  `trade_chating` tinyint(3) NOT NULL DEFAULT '1',
  `whisper_chating` tinyint(3) NOT NULL DEFAULT '1',
  `attribute` tinyint(3) unsigned NOT NULL DEFAULT '0',
  `lvStr` int(10) unsigned NOT NULL DEFAULT '0',
  `lvCon` int(10) unsigned NOT NULL DEFAULT '0',
  `lvDex` int(10) unsigned NOT NULL DEFAULT '0',
  `lvWis` int(10) unsigned NOT NULL DEFAULT '0',
  `lvInt` int(10) unsigned NOT NULL DEFAULT '0',
  `lvCha` int(10) unsigned NOT NULL DEFAULT '0',
  `elixir` tinyint(3) unsigned NOT NULL DEFAULT '0',
  `elixirStr` tinyint(3) unsigned NOT NULL DEFAULT '0',
  `elixirCon` tinyint(3) unsigned NOT NULL DEFAULT '0',
  `elixirDex` tinyint(3) unsigned NOT NULL DEFAULT '0',
  `elixirWis` tinyint(3) unsigned NOT NULL DEFAULT '0',
  `elixirInt` tinyint(3) unsigned NOT NULL DEFAULT '0',
  `elixirCha` tinyint(3) unsigned NOT NULL DEFAULT '0',
  `statreset` tinyint(3) unsigned NOT NULL DEFAULT '0',
  `age` tinyint(2) unsigned NOT NULL DEFAULT '0',
  `register_date` bigint(20) unsigned NOT NULL DEFAULT '0',
  `join_date` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  `end_date` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' ON UPDATE CURRENT_TIMESTAMP,
  `block_date` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  `save_interface` text NOT NULL,
  `speedhack_warning_count` int(10) unsigned NOT NULL DEFAULT '0',
  `monster_kill_count` int(10) unsigned NOT NULL DEFAULT '0',
  `clan_grade` tinyint(2) NOT NULL DEFAULT '0',
  `level_up_stat` tinyint(3) NOT NULL DEFAULT '0',
  `reset_base_stat` tinyint(3) NOT NULL DEFAULT '0',
  `reset_level_stat` tinyint(3) NOT NULL DEFAULT '0',
  `quick_polymorph` varchar(20) NOT NULL DEFAULT '',
  `is_auto_potion` tinyint(3) NOT NULL DEFAULT '0',
  `auto_potion_percent` tinyint(3) NOT NULL DEFAULT '0',
  `auto_potion` varchar(50) NOT NULL DEFAULT '',
  `auto_hunt_monster_count` int(10) unsigned NOT NULL DEFAULT '0',
  `evolution_count` tinyint(3) NOT NULL DEFAULT '0',
  `temp_name` varchar(20) NOT NULL DEFAULT '',
  `temp_clan_name` varchar(20) NOT NULL DEFAULT '',
  `temp_clan_id` mediumint(10) NOT NULL DEFAULT '0',
  `temp_clan_grade` tinyint(2) NOT NULL DEFAULT '0',
  `temp_title` varchar(20) NOT NULL DEFAULT '',
  `battle_team` tinyint(2) NOT NULL DEFAULT '0',
  `last_ip` varchar(20) NOT NULL DEFAULT '',
  `장인주문서_사용횟수` int(10) NOT NULL DEFAULT '0',
  `경험치저장구슬_사용횟수` int(10) NOT NULL DEFAULT '0',
  `경험치구슬_사용횟수` int(10) NOT NULL DEFAULT '0',
  `자동사냥_남은시간` int(10) NOT NULL DEFAULT '0',
  `자동사냥_귀환체력` tinyint(3) NOT NULL DEFAULT '0',
  `자동사냥_자동버프` tinyint(3) NOT NULL DEFAULT '0',
  `자동사냥_물약구매` tinyint(3) NOT NULL DEFAULT '0',
  `자동사냥_우선변줌사용` tinyint(3) NOT NULL DEFAULT '0',
  `자동사냥_자동랭변` tinyint(3) NOT NULL DEFAULT '0',
  `자동사냥_랭변구매` tinyint(3) NOT NULL DEFAULT '0',
  `자동사냥_자동변신` tinyint(3) NOT NULL DEFAULT '0',
  `자동사냥_변줌구매` tinyint(3) NOT NULL DEFAULT '0',
  `자동사냥_자동텔포` tinyint(3) NOT NULL DEFAULT '0',
  `자동사냥_순줌구매` tinyint(3) NOT NULL DEFAULT '0',
  `자동사냥_자동용기` tinyint(3) NOT NULL DEFAULT '0',
  `자동사냥_용기구매` tinyint(3) NOT NULL DEFAULT '0',
  `자동사냥_자동촐기` tinyint(3) NOT NULL DEFAULT '0',
  `자동사냥_촐기구매` tinyint(3) NOT NULL DEFAULT '0',
  `자동사냥_화살구매` tinyint(3) NOT NULL DEFAULT '0',
  PRIMARY KEY (`name`,`account`),
  KEY `name` (`name`),
  KEY `account` (`account`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of characters
-- ----------------------------

-- ----------------------------
-- Table structure for `characters_block_list`
-- ----------------------------
DROP TABLE IF EXISTS `characters_block_list`;
CREATE TABLE `characters_block_list` (
  `cha_objId` int(10) NOT NULL DEFAULT '0',
  `cha_name` varchar(20) NOT NULL DEFAULT '',
  `block_name` varchar(20) NOT NULL DEFAULT '',
  PRIMARY KEY (`cha_objId`,`block_name`),
  KEY `cha_objId` (`cha_objId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of characters_block_list
-- ----------------------------

-- ----------------------------
-- Table structure for `characters_book`
-- ----------------------------
DROP TABLE IF EXISTS `characters_book`;
CREATE TABLE `characters_book` (
  `objId` int(10) unsigned NOT NULL DEFAULT '0',
  `name` varchar(20) NOT NULL DEFAULT '',
  `location` varchar(100) NOT NULL DEFAULT '',
  `locX` int(10) unsigned NOT NULL DEFAULT '0',
  `locY` int(10) unsigned NOT NULL DEFAULT '0',
  `locMAP` int(10) unsigned NOT NULL DEFAULT '0',
  PRIMARY KEY (`objId`,`location`),
  KEY `objId` (`objId`),
  KEY `location` (`location`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of characters_book
-- ----------------------------

-- ----------------------------
-- Table structure for `characters_buff`
-- ----------------------------
DROP TABLE IF EXISTS `characters_buff`;
CREATE TABLE `characters_buff` (
  `objId` int(10) unsigned NOT NULL DEFAULT '0',
  `name` varchar(20) NOT NULL DEFAULT '',
  `light` int(10) NOT NULL DEFAULT '0',
  `shield` int(10) NOT NULL DEFAULT '0',
  `curse_poison` int(10) NOT NULL DEFAULT '0',
  `curse_poison_level` int(10) NOT NULL DEFAULT '0',
  `curse_blind` int(10) NOT NULL DEFAULT '0',
  `slow` int(10) NOT NULL DEFAULT '0',
  `curse_paralyze` int(10) NOT NULL DEFAULT '0',
  `enchant_dexterity` int(10) NOT NULL DEFAULT '0',
  `enchant_mighty` int(10) NOT NULL DEFAULT '0',
  `haste` int(10) NOT NULL DEFAULT '0',
  `haset_potion` int(10) NOT NULL DEFAULT '0',
  `invisibility` int(10) NOT NULL DEFAULT '0',
  `shape_change` int(10) NOT NULL DEFAULT '0',
  `immune_to_harm` int(10) NOT NULL DEFAULT '0',
  `bravery_potion` int(10) NOT NULL DEFAULT '0',
  `elvenwafer` int(10) NOT NULL DEFAULT '0',
  `eva` int(10) NOT NULL DEFAULT '0',
  `wisdom` int(10) NOT NULL DEFAULT '0',
  `blue_potion` int(10) NOT NULL DEFAULT '0',
  `floating_eye_meat` int(10) NOT NULL DEFAULT '0',
  `clearmind` int(10) NOT NULL DEFAULT '0',
  `resistelemental` int(10) NOT NULL DEFAULT '0',
  `icelance` int(10) NOT NULL DEFAULT '0',
  `earthskin` int(10) NOT NULL DEFAULT '0',
  `ironskin` int(10) NOT NULL DEFAULT '0',
  `blessearth` int(10) NOT NULL DEFAULT '0',
  `curse_ghoul` int(10) NOT NULL DEFAULT '0',
  `curse_ghast` int(10) NOT NULL DEFAULT '0',
  `chatting_close` int(10) NOT NULL DEFAULT '0',
  `holywalk` int(10) NOT NULL DEFAULT '0',
  `shadowarmor` int(10) NOT NULL DEFAULT '0',
  `exp_potion` int(10) NOT NULL DEFAULT '0',
  `frame_speed_stun` int(10) NOT NULL DEFAULT '0',
  `buff_fight` int(10) NOT NULL DEFAULT '0',
  `수룡의_마안` int(10) NOT NULL DEFAULT '0',
  `풍룡의_마안` int(10) NOT NULL DEFAULT '0',
  `지룡의_마안` int(10) NOT NULL DEFAULT '0',
  `화룡의_마안` int(10) NOT NULL DEFAULT '0',
  `생명의_마안` int(10) NOT NULL DEFAULT '0',
  `탄생의_마안` int(10) NOT NULL DEFAULT '0',
  `형상의_마안` int(10) NOT NULL DEFAULT '0',
  `수룡의마안_딜레이` int(10) NOT NULL DEFAULT '0',
  `풍룡의마안_딜레이` int(10) NOT NULL DEFAULT '0',
  `지룡의마안_딜레이` int(10) NOT NULL DEFAULT '0',
  `화룡의마안_딜레이` int(10) NOT NULL DEFAULT '0',
  `생명의마안_딜레이` int(10) NOT NULL DEFAULT '0',
  `탄생의마안_딜레이` int(10) NOT NULL DEFAULT '0',
  `형상의마안_딜레이` int(10) NOT NULL DEFAULT '0',
  `경험치_드랍_10` int(10) NOT NULL DEFAULT '0',
  `경험치_드랍_20` int(10) NOT NULL DEFAULT '0',
  `경험치_드랍_50` int(10) NOT NULL DEFAULT '0',
  `복수_쿨타임` int(10) NOT NULL DEFAULT '0',
  PRIMARY KEY (`objId`,`name`),
  KEY `objId` (`objId`),
  KEY `name` (`name`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;


-- ----------------------------
-- Records of characters_buff
-- ----------------------------

-- ----------------------------
-- Table structure for `characters_friend`
-- ----------------------------
DROP TABLE IF EXISTS `characters_friend`;
CREATE TABLE `characters_friend` (
  `name` varchar(255) NOT NULL DEFAULT '',
  `object_id` int(10) unsigned NOT NULL DEFAULT '0',
  `friend` varchar(255) NOT NULL DEFAULT '',
  PRIMARY KEY (`object_id`,`friend`),
  KEY `object_id` (`object_id`),
  KEY `friend` (`friend`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of characters_friend
-- ----------------------------

-- ----------------------------
-- Table structure for `characters_inventory`
-- ----------------------------
DROP TABLE IF EXISTS `characters_inventory`;
CREATE TABLE `characters_inventory` (
  `objId` int(10) unsigned NOT NULL,
  `cha_objId` int(10) unsigned NOT NULL,
  `cha_name` varchar(20) NOT NULL DEFAULT '',
  `name` varchar(50) NOT NULL DEFAULT '',
  `count` bigint(20) unsigned NOT NULL DEFAULT '0',
  `quantity` int(10) unsigned NOT NULL DEFAULT '0',
  `en` int(10) DEFAULT '0',
  `equipped` tinyint(1) unsigned NOT NULL DEFAULT '0',
  `definite` tinyint(1) unsigned NOT NULL DEFAULT '0',
  `bress` tinyint(1) DEFAULT '1',
  `durability` tinyint(1) unsigned NOT NULL DEFAULT '0',
  `nowtime` int(10) unsigned NOT NULL DEFAULT '0',
  `pet_objid` int(10) unsigned NOT NULL DEFAULT '0',
  `inn_key` int(10) NOT NULL DEFAULT '0',
  `letter_uid` int(10) NOT NULL DEFAULT '0',
  `slimerace` varchar(255) NOT NULL DEFAULT '',
  `구분1` varchar(30) NOT NULL DEFAULT '',
  `구분2` varchar(30) NOT NULL DEFAULT '',
  `options` varchar(255) NOT NULL DEFAULT '',
  `enfire` int(10) NOT NULL DEFAULT '0',
  PRIMARY KEY (`objId`,`cha_objId`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of characters_inventory
-- ----------------------------

-- ----------------------------
-- Table structure for `characters_letter`
-- ----------------------------
DROP TABLE IF EXISTS `characters_letter`;
CREATE TABLE `characters_letter` (
  `uid` int(10) unsigned NOT NULL DEFAULT '0',
  `type` enum('savePaper','clanPaper','Paper') NOT NULL,
  `paperFrom` varchar(20) NOT NULL,
  `paperTo` varchar(20) NOT NULL,
  `paperSubject` varchar(20) NOT NULL,
  `paperMemo` text NOT NULL,
  `paperInventory` enum('true','false') NOT NULL DEFAULT 'false',
  `paperAden` int(10) unsigned NOT NULL DEFAULT '0',
  `paperDate` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  `paperRead` tinyint(1) unsigned NOT NULL DEFAULT '0',
  PRIMARY KEY (`uid`),
  KEY `uid` (`uid`),
  KEY `type` (`type`),
  KEY `paperTo` (`paperTo`),
  KEY `paperInventory` (`paperInventory`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of characters_letter
-- ----------------------------

-- ----------------------------
-- Table structure for `characters_pet`
-- ----------------------------
DROP TABLE IF EXISTS `characters_pet`;
CREATE TABLE `characters_pet` (
  `objid` int(10) unsigned NOT NULL DEFAULT '0',
  `name` varchar(20) NOT NULL DEFAULT '',
  `classId` int(10) unsigned NOT NULL,
  `level` int(10) unsigned NOT NULL DEFAULT '0',
  `nowHp` int(10) unsigned NOT NULL DEFAULT '0',
  `maxHp` int(10) unsigned NOT NULL DEFAULT '0',
  `nowMp` int(10) unsigned NOT NULL DEFAULT '0',
  `maxMp` int(10) unsigned NOT NULL DEFAULT '0',
  `exp` int(10) unsigned NOT NULL DEFAULT '0',
  `lawful` int(10) unsigned NOT NULL DEFAULT '0',
  `gfx` int(10) unsigned NOT NULL DEFAULT '0',
  `food_mode` tinyint(3) NOT NULL DEFAULT '0',
  `del` enum('true','false') NOT NULL DEFAULT 'false',
  PRIMARY KEY (`objid`),
  KEY `objid` (`objid`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of characters_pet
-- ----------------------------

-- ----------------------------
-- Table structure for `characters_pvp`
-- ----------------------------
DROP TABLE IF EXISTS `characters_pvp`;
CREATE TABLE `characters_pvp` (
  `uid` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `objectId` int(10) unsigned NOT NULL,
  `name` varchar(20) NOT NULL DEFAULT '',
  `is_kill` tinyint(3) unsigned NOT NULL DEFAULT '0',
  `is_dead` tinyint(3) unsigned NOT NULL DEFAULT '0',
  `target_objectId` int(10) unsigned NOT NULL,
  `target_name` varchar(20) NOT NULL DEFAULT '',
  `pvp_date` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  PRIMARY KEY (`uid`),
  KEY `objectId` (`objectId`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of characters_pvp
-- ----------------------------

-- ----------------------------
-- Table structure for `characters_quest`
-- ----------------------------
DROP TABLE IF EXISTS `characters_quest`;
CREATE TABLE `characters_quest` (
  `objId` int(10) unsigned NOT NULL DEFAULT '0',
  `name` varchar(255) NOT NULL DEFAULT '',
  `npc_name` varchar(255) NOT NULL DEFAULT '',
  `quest_action` varchar(255) NOT NULL DEFAULT '',
  `quest_step` tinyint(3) unsigned NOT NULL DEFAULT '0',
  PRIMARY KEY (`objId`,`quest_action`),
  KEY `objId` (`objId`),
  KEY `quest_action` (`quest_action`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of characters_quest
-- ----------------------------

-- ----------------------------
-- Table structure for `characters_skill`
-- ----------------------------
DROP TABLE IF EXISTS `characters_skill`;
CREATE TABLE `characters_skill` (
  `uid` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `cha_objId` int(10) unsigned NOT NULL DEFAULT '0',
  `cha_name` varchar(20) NOT NULL DEFAULT '',
  `skill` int(10) unsigned NOT NULL DEFAULT '0',
  `skill_name` varchar(50) NOT NULL DEFAULT '',
  PRIMARY KEY (`uid`,`cha_objId`,`skill`),
  KEY `cha_objId` (`cha_objId`),
  KEY `skill` (`skill`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of characters_skill
-- ----------------------------

-- ----------------------------
-- Table structure for `characters_swap`
-- ----------------------------
DROP TABLE IF EXISTS `characters_swap`;
CREATE TABLE `characters_swap` (
  `cha_objId` int(10) NOT NULL DEFAULT '0',
  `swap_name` varchar(255) NOT NULL DEFAULT '',
  `swap_idx` tinyint(3) NOT NULL DEFAULT '0',
  `swap_item_objId` int(10) NOT NULL DEFAULT '0',
  `swap_item_name` varchar(255) NOT NULL DEFAULT '',
  `swap_item_bless` tinyint(3) NOT NULL DEFAULT '0',
  `swap_item_en` tinyint(3) NOT NULL DEFAULT '0',
  PRIMARY KEY (`cha_objId`,`swap_name`,`swap_idx`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of characters_swap
-- ----------------------------

-- ----------------------------
-- Table structure for `clan_list`
-- ----------------------------
DROP TABLE IF EXISTS `clan_list`;
CREATE TABLE `clan_list` (
  `uid` int(10) unsigned NOT NULL DEFAULT '0',
  `name` varchar(20) NOT NULL DEFAULT '',
  `lord` varchar(20) NOT NULL DEFAULT '',
  `icon` text NOT NULL,
  `list` text NOT NULL,
  `point` int(10) NOT NULL DEFAULT '0',
  `sell_point` int(10) NOT NULL DEFAULT '0',
  `total_point` int(10) NOT NULL DEFAULT '0',
  `경험치증가` int(10) NOT NULL DEFAULT '0',
  `드랍확률증가` int(10) NOT NULL DEFAULT '0',
  `아덴증가` int(10) NOT NULL DEFAULT '0',
  `추타` int(10) NOT NULL DEFAULT '0',
  `리덕` int(10) NOT NULL DEFAULT '0',
  `pvp_추타` int(10) NOT NULL DEFAULT '0',
  `pvp_리덕` int(10) NOT NULL DEFAULT '0',
  `스턴내성` int(10) NOT NULL DEFAULT '0',
  `치명타확률` int(10) NOT NULL DEFAULT '0',
  `sp` int(10) NOT NULL DEFAULT '0',
  PRIMARY KEY (`uid`),
  KEY `uid` (`uid`),
  KEY `name` (`name`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of clan_list
-- ----------------------------
INSERT INTO `clan_list` VALUES ('1', '신규', '', '0000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000008c7d0000ac7e0000000000006c7fcc7f00000000ec5f000000000000f73300008c7dac7e00006c7f00006c7f00000000ec5f0000ec3b0000f7330000ff3200004c7e000000006c7f0000cc7f00000000ec3b0000f7330000ff3200001f3200004c7e000000006c7f0000ec6f00000000f73300007f3300007f3200009f410000ac7e00000000cc7f0000ec5fec3bf73300000000ff3200001f3200009f5100002c7f00000000ec6f0000ec3b00000000000000007f3200009f4100009f5d00006c7f00000000ec5f0000f73300000000000000001f3200009f5100009f710000ec6f00000000f73300000000ff33ff327f32000000009f519f5d9f71000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000', ' ', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0');
INSERT INTO `clan_list` VALUES ('2', 'A팀', '', '00000000000000000000000000000000000000000000000000000000000000000000c07fc07fc07f00000000000000000000000000000000c07fc07fc07f000000000000c07fc07fc07f000000000000000000000000c07fc07fc07f00000000000000000000c07fc07fc07f0000000000000000c07fc07fc07f0000000000000000000000000000c07fc07fc07f00000000c07fc07fc07f000000000000000000000000000000000000c07fc07fc07fc07fc07fc07f0000000000000000000000000000000000000000c07fc07fc07fc07fc07fc07f000000000000000000000000000000000000c07fc07fc07f00000000c07fc07fc07f0000000000000000000000000000c07fc07fc07f0000000000000000c07fc07fc07f00000000000000000000c07fc07fc07f000000000000000000000000c07fc07fc07f000000000000c07fc07fc07f00000000000000000000000000000000c07fc07fc07f00000000000000000000000000000000000000000000000000000000000000000000', ' ', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0');
INSERT INTO `clan_list` VALUES ('3', 'B팀', '', '00000000000000000000000000000000000000000000000000000000000000000000007c007c007c00000000000000000000000000000000007c007c007c000000000000007c007c007c000000000000000000000000007c007c007c00000000000000000000007c007c007c0000000000000000007c007c007c0000000000000000000000000000007c007c007c00000000007c007c007c000000000000000000000000000000000000007c007c007c007c007c007c0000000000000000000000000000000000000000007c007c007c007c007c007c000000000000000000000000000000000000007c007c007c00000000007c007c007c0000000000000000000000000000007c007c007c0000000000000000007c007c007c00000000000000000000007c007c007c000000000000000000000000007c007c007c000000000000007c007c007c00000000000000000000000000000000007c007c007c00000000000000000000000000000000000000000000000000000000000000000000', ' ', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0');

-- ----------------------------
-- Table structure for `kingdom`
-- ----------------------------
DROP TABLE IF EXISTS `kingdom`;
CREATE TABLE `kingdom` (
  `uid` int(4) unsigned NOT NULL DEFAULT '0',
  `name` varchar(50) NOT NULL DEFAULT '',
  `x` int(10) unsigned NOT NULL DEFAULT '0',
  `y` int(10) unsigned NOT NULL DEFAULT '0',
  `map` int(10) unsigned NOT NULL DEFAULT '0',
  `throne_x` int(10) unsigned NOT NULL DEFAULT '0',
  `throne_y` int(10) unsigned NOT NULL DEFAULT '0',
  `throne_map` int(10) unsigned NOT NULL DEFAULT '0',
  `clan_id` int(4) unsigned NOT NULL DEFAULT '0',
  `clan_name` varchar(50) NOT NULL DEFAULT '',
  `agent_id` int(4) unsigned NOT NULL DEFAULT '0',
  `agent_name` varchar(100) NOT NULL DEFAULT '',
  `tax_rate` int(3) unsigned NOT NULL DEFAULT '0',
  `tax_rate_tomorrow` int(3) unsigned NOT NULL DEFAULT '0',
  `tax_day` datetime NOT NULL DEFAULT '0000-00-00 00:00:00',
  `tax_total` int(10) unsigned NOT NULL DEFAULT '0',
  `war` enum('true','false') NOT NULL DEFAULT 'false',
  `war_day` datetime NOT NULL DEFAULT '0000-00-00 00:00:00',
  `war_day_end` datetime NOT NULL DEFAULT '0000-00-00 00:00:00',
  `war_list` varchar(255) NOT NULL,
  PRIMARY KEY (`uid`),
  KEY `uid` (`uid`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of kingdom
-- ----------------------------
INSERT INTO `kingdom` VALUES ('4', '기란 성', '32729', '32803', '52', '32729', '32788', '52', '0', '', '0', '', '1', '1', '2017-12-13 18:10:41', '0', 'false', '2017-12-13 21:00:00', '2017-11-22 16:55:53', '');

-- ----------------------------
-- Table structure for `kingdom_tax_log`
-- ----------------------------
DROP TABLE IF EXISTS `kingdom_tax_log`;
CREATE TABLE `kingdom_tax_log` (
  `kingdom` tinyint(3) unsigned NOT NULL,
  `kingdom_name` varchar(255) NOT NULL DEFAULT '',
  `type` enum('shop','agit','tribute','peace','payment_servants','upkeep','payment_mercenaries','miscellaneous') NOT NULL,
  `tax` int(10) unsigned NOT NULL DEFAULT '0',
  `date` date NOT NULL DEFAULT '0000-00-00',
  PRIMARY KEY (`kingdom`,`type`,`date`),
  KEY `date` (`date`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of kingdom_tax_log
-- ----------------------------

-- ----------------------------
-- Table structure for `pc_shop`
-- ----------------------------
DROP TABLE IF EXISTS `pc_shop`;
CREATE TABLE `pc_shop` (
  `pc_objId` int(10) NOT NULL DEFAULT '0',
  `pc_name` varchar(20) NOT NULL DEFAULT '',
  `item_objId` int(10) NOT NULL DEFAULT '0',
  `item_name` varchar(50) NOT NULL DEFAULT '',
  `bless` tinyint(3) NOT NULL DEFAULT '1',
  `en_level` tinyint(3) NOT NULL DEFAULT '0',
  `definite` enum('false','true') NOT NULL DEFAULT 'true',
  `count` bigint(20) NOT NULL DEFAULT '0',
  `aden_type` varchar(20) NOT NULL DEFAULT '',
  `price` bigint(20) NOT NULL DEFAULT '0',
  `enfire` int(10) NOT NULL DEFAULT '0',
  PRIMARY KEY (`item_objId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
-- ----------------------------
-- Records of pc_shop
-- ----------------------------

-- ----------------------------
-- Table structure for `pc_shop_robot`
-- ----------------------------
DROP TABLE IF EXISTS `pc_shop_robot`;
CREATE TABLE `pc_shop_robot` (
  `pc_objId` int(10) NOT NULL,
  `pc_name` varchar(20) NOT NULL DEFAULT '',
  `loc_x` int(10) NOT NULL,
  `loc_y` int(10) NOT NULL,
  `loc_map` int(10) NOT NULL DEFAULT '5000',
  `class_type` tinyint(1) NOT NULL DEFAULT '0',
  `class_sex` tinyint(1) NOT NULL DEFAULT '0',
  `heading` tinyint(3) NOT NULL,
  `ment` varchar(255) NOT NULL DEFAULT '',
  PRIMARY KEY (`pc_objId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of pc_shop_robot
-- ----------------------------

-- ----------------------------
-- Table structure for `pc_trade`
-- ----------------------------
DROP TABLE IF EXISTS `pc_trade`;
CREATE TABLE `pc_trade` (
  `uid` int(10) NOT NULL DEFAULT '0',
  `sell_account_uid` int(10) NOT NULL DEFAULT '0',
  `sell_name` varchar(255) NOT NULL DEFAULT '',
  `sell_objId` int(10) NOT NULL DEFAULT '0',
  `state` varchar(255) NOT NULL DEFAULT '',
  `buy_account_uid` int(10) NOT NULL DEFAULT '0',
  `buy_name` varchar(255) NOT NULL DEFAULT '',
  `buy_objId` int(10) NOT NULL DEFAULT '0',
  `price` int(10) NOT NULL DEFAULT '0',
  `item_objId` bigint(20) NOT NULL DEFAULT '0',
  `item` varchar(255) NOT NULL DEFAULT '',
  `enchant` int(10) NOT NULL DEFAULT '0',
  `bless` int(10) NOT NULL DEFAULT '0',
  `count` bigint(20) NOT NULL DEFAULT '0',
  `write_day` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `buy_apply_day` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  `complete_day` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  `subject` varchar(255) NOT NULL DEFAULT '',
  `content` text NOT NULL,
  `name` varchar(255) NOT NULL DEFAULT '',
  `phone_num` varchar(255) NOT NULL DEFAULT '',
  `bank_name` varchar(255) NOT NULL DEFAULT '',
  `bank_num` varchar(255) NOT NULL DEFAULT '',
  PRIMARY KEY (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of pc_trade
-- ----------------------------

-- ----------------------------
-- Table structure for `promote`
-- ----------------------------
DROP TABLE IF EXISTS `promote`;
CREATE TABLE `promote` (
  `name` varchar(50) NOT NULL,
  `item_name` enum('홍보 보상 상자') NOT NULL DEFAULT '홍보 보상 상자',
  `count` tinyint(3) NOT NULL DEFAULT '1',
  PRIMARY KEY (`name`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of promote
-- ----------------------------

-- ----------------------------
-- Table structure for `server`
-- ----------------------------
DROP TABLE IF EXISTS `server`;
CREATE TABLE `server` (
  `name` varchar(255) NOT NULL,
  `sever_open_date` datetime NOT NULL DEFAULT '0000-00-00 00:00:00',
  `pc_objid` int(10) unsigned NOT NULL DEFAULT '1000000',
  `item_objid` int(10) unsigned NOT NULL DEFAULT '2000000',
  `pc_trade_uid` int(10) NOT NULL DEFAULT '0',
  `player_count` int(10) unsigned NOT NULL DEFAULT '0',
  `inn_key` int(10) unsigned NOT NULL DEFAULT '1',
  `running` enum('true','false') NOT NULL DEFAULT 'false',
  `web_objid` int(10) unsigned NOT NULL DEFAULT '100000000',
  `world_time` double(20,0) NOT NULL,
  PRIMARY KEY (`name`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of server
-- ----------------------------
INSERT INTO `server` VALUES ('서버', '2018-07-23 19:00:00', '1000000', '2000000', '0', '0', '0', 'false', '100000000', '0');

-- ----------------------------
-- Table structure for `server_down_bosslist`
-- ----------------------------
DROP TABLE IF EXISTS `server_down_bosslist`;
CREATE TABLE `server_down_bosslist` (
  `objId` bigint(20) NOT NULL DEFAULT '0',
  `monster` varchar(255) NOT NULL DEFAULT '',
  `now_hp` int(10) NOT NULL DEFAULT '0',
  `now_mp` int(10) NOT NULL DEFAULT '0',
  `home_x` int(5) NOT NULL DEFAULT '0',
  `home_y` int(5) NOT NULL DEFAULT '0',
  `home_map` int(10) NOT NULL DEFAULT '0',
  `x` int(5) NOT NULL DEFAULT '0',
  `y` int(5) NOT NULL DEFAULT '0',
  `map` int(10) NOT NULL DEFAULT '0',
  `heading` tinyint(2) NOT NULL DEFAULT '0',
  `live_time` int(10) NOT NULL DEFAULT '0',
  PRIMARY KEY (`objId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of server_down_bosslist
-- ----------------------------

-- ----------------------------
-- Table structure for `wanted`
-- ----------------------------
DROP TABLE IF EXISTS `wanted`;
CREATE TABLE `wanted` (
  `objId` int(10) NOT NULL DEFAULT '0',
  `name` varchar(10) NOT NULL DEFAULT '',
  `price` double(20,0) NOT NULL DEFAULT '0',
  `date` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`objId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of wanted
-- ----------------------------

-- ----------------------------
-- Table structure for `warehouse`
-- ----------------------------
DROP TABLE IF EXISTS `warehouse`;
CREATE TABLE `warehouse` (
  `uid` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `account_uid` int(10) unsigned NOT NULL DEFAULT '0',
  `inv_id` int(20) unsigned NOT NULL DEFAULT '0',
  `pet_id` int(10) unsigned NOT NULL DEFAULT '0',
  `letter_id` int(10) unsigned NOT NULL DEFAULT '0',
  `name` varchar(50) NOT NULL DEFAULT '',
  `type` tinyint(3) unsigned NOT NULL,
  `gfxid` int(2) NOT NULL DEFAULT '0',
  `count` int(10) unsigned NOT NULL DEFAULT '0',
  `quantity` tinyint(3) unsigned NOT NULL DEFAULT '0',
  `en` tinyint(3) NOT NULL DEFAULT '0',
  `definite` tinyint(3) unsigned NOT NULL DEFAULT '0',
  `bress` tinyint(3) NOT NULL DEFAULT '0',
  `durability` tinyint(3) unsigned NOT NULL DEFAULT '0',
  `time` int(10) NOT NULL,
  `enfire` int(10) NOT NULL DEFAULT '0',
  PRIMARY KEY (`uid`),
  KEY `account_uid` (`account_uid`)
) ENGINE=MyISAM AUTO_INCREMENT=8 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of warehouse
-- ----------------------------

-- ----------------------------
-- Table structure for `warehouse_clan`
-- ----------------------------
DROP TABLE IF EXISTS `warehouse_clan`;
CREATE TABLE `warehouse_clan` (
  `uid` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `clan_id` int(10) unsigned NOT NULL DEFAULT '0',
  `inv_id` int(10) unsigned NOT NULL DEFAULT '0',
  `pet_id` int(10) unsigned NOT NULL DEFAULT '0',
  `letter_id` int(10) unsigned NOT NULL DEFAULT '0',
  `name` varchar(50) NOT NULL DEFAULT '',
  `type` tinyint(3) unsigned NOT NULL,
  `gfxid` int(2) NOT NULL DEFAULT '0',
  `count` int(10) unsigned NOT NULL DEFAULT '0',
  `quantity` tinyint(3) unsigned NOT NULL DEFAULT '0',
  `en` tinyint(3) unsigned NOT NULL DEFAULT '0',
  `definite` tinyint(3) unsigned NOT NULL DEFAULT '0',
  `bress` tinyint(3) NOT NULL DEFAULT '0',
  `durability` tinyint(3) unsigned NOT NULL DEFAULT '0',
  `time` int(10) NOT NULL,
  `enfire` int(10) NOT NULL DEFAULT '0',
  PRIMARY KEY (`uid`),
  KEY `clan_id` (`clan_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of warehouse_clan
-- ----------------------------

-- ----------------------------
-- Table structure for `warehouse_clan_log`
-- ----------------------------
DROP TABLE IF EXISTS `warehouse_clan_log`;
CREATE TABLE `warehouse_clan_log` (
  `uid` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `type` enum('append','remove') NOT NULL DEFAULT 'append',
  `character_uid` int(10) unsigned NOT NULL DEFAULT '0',
  `character_name` varchar(20) NOT NULL DEFAULT '',
  `clan_uid` int(10) unsigned NOT NULL DEFAULT '0',
  `clan_name` varchar(20) NOT NULL DEFAULT '',
  `item_uid` int(10) unsigned NOT NULL DEFAULT '0',
  `item_name` varchar(50) NOT NULL DEFAULT '',
  `item_count` int(10) unsigned NOT NULL DEFAULT '0',
  `item_en` tinyint(3) DEFAULT NULL,
  `item_bress` tinyint(3) DEFAULT NULL,
  PRIMARY KEY (`uid`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of warehouse_clan_log
-- ----------------------------

-- ----------------------------
-- Table structure for `warehouse_elf`
-- ----------------------------
DROP TABLE IF EXISTS `warehouse_elf`;
CREATE TABLE `warehouse_elf` (
  `uid` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `account_uid` int(10) unsigned NOT NULL DEFAULT '0',
  `inv_id` int(10) unsigned NOT NULL DEFAULT '0',
  `pet_id` int(10) unsigned NOT NULL DEFAULT '0',
  `letter_id` int(10) unsigned NOT NULL DEFAULT '0',
  `name` varchar(50) NOT NULL DEFAULT '',
  `type` tinyint(3) unsigned NOT NULL,
  `gfxid` int(2) NOT NULL DEFAULT '0',
  `count` int(10) unsigned NOT NULL DEFAULT '0',
  `quantity` tinyint(3) unsigned NOT NULL DEFAULT '0',
  `en` tinyint(3) NOT NULL DEFAULT '0',
  `definite` tinyint(3) unsigned NOT NULL DEFAULT '0',
  `bress` tinyint(3) NOT NULL DEFAULT '0',
  `durability` tinyint(3) unsigned NOT NULL DEFAULT '0',
  `time` int(10) NOT NULL,
  `enfire` int(10) NOT NULL DEFAULT '0',
  PRIMARY KEY (`uid`),
  KEY `account_uid` (`account_uid`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of warehouse_elf
-- ----------------------------

-- ----------------------------
-- Table structure for `race_log`
-- ----------------------------
DROP TABLE IF EXISTS `race_log`;
CREATE TABLE `race_log` (
  `uid` int(10) NOT NULL,
  `type` enum('slime','dog','fight') NOT NULL DEFAULT 'slime',
  `race_idx` int(10) unsigned NOT NULL,
  `price` int(10) unsigned NOT NULL DEFAULT '0',
  `rate` varchar(255) NOT NULL DEFAULT '',
  `cnt_buy` bigint(20) NOT NULL DEFAULT '0',
  `cnt_sell` bigint(20) NOT NULL DEFAULT '0',
  `sell_price` int(10) NOT NULL DEFAULT '0',
  PRIMARY KEY (`uid`,`type`),
  KEY `type` (`type`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of race_log
-- ----------------------------

-- ----------------------------
-- Table structure for `boards_auction`
-- ----------------------------
DROP TABLE IF EXISTS `boards_auction`;
CREATE TABLE `boards_auction` (
  `uid` int(4) unsigned NOT NULL DEFAULT '0',
  `type` enum('giran','heine','aden') NOT NULL,
  `loc` varchar(20) NOT NULL DEFAULT '',
  `size` int(1) unsigned NOT NULL DEFAULT '0',
  `sell` enum('true','false') NOT NULL DEFAULT 'true',
  `agent` varchar(100) NOT NULL DEFAULT '',
  `bidder` varchar(100) NOT NULL DEFAULT '',
  `price` int(10) unsigned NOT NULL DEFAULT '0',
  `day` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  `default_price` int(10) NOT NULL DEFAULT '0',
  PRIMARY KEY (`uid`),
  KEY `uid` (`uid`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of boards_auction
-- ----------------------------
INSERT INTO `boards_auction` VALUES ('458773', 'aden', '34015, 33374', '24', 'false', '', '', '50000000', '2011-06-01 12:00:00', '50000000');
INSERT INTO `boards_auction` VALUES ('458772', 'aden', '34027, 33382', '64', 'false', '', '', '50000000', '2011-06-01 12:00:00', '50000000');
INSERT INTO `boards_auction` VALUES ('458771', 'aden', '34078, 33370', '32', 'false', '', '', '50000000', '2011-06-01 12:00:00', '50000000');
INSERT INTO `boards_auction` VALUES ('458769', 'aden', '34061, 33376', '63', 'false', '', '', '50000000', '2011-06-01 12:00:00', '50000000');
INSERT INTO `boards_auction` VALUES ('458770', 'aden', '34075, 33382', '24', 'false', '', '', '50000000', '2011-06-01 12:00:00', '50000000');
INSERT INTO `boards_auction` VALUES ('458766', 'aden', '34127, 33368', '63', 'false', '', '', '50000000', '2011-06-01 12:00:00', '50000000');
INSERT INTO `boards_auction` VALUES ('458767', 'aden', '34091, 33350', '40', 'false', '', '', '50000000', '2011-06-01 12:00:00', '50000000');
INSERT INTO `boards_auction` VALUES ('458768', 'aden', '34098, 33370', '56', 'false', '', '', '50000000', '2011-06-01 12:00:00', '50000000');
INSERT INTO `boards_auction` VALUES ('458765', 'aden', '34128, 33354', '24', 'false', '', '', '50000000', '2011-06-01 12:00:00', '50000000');
INSERT INTO `boards_auction` VALUES ('458764', 'aden', '34074, 33356', '63', 'false', '', '', '50000000', '2011-06-01 12:00:00', '50000000');
INSERT INTO `boards_auction` VALUES ('458763', 'aden', '34147, 33370', '24', 'false', '', '', '50000000', '2011-06-01 12:00:00', '50000000');
INSERT INTO `boards_auction` VALUES ('458762', 'aden', '34064, 33397', '24', 'false', '', '', '50000000', '2011-06-01 12:00:00', '50000000');
INSERT INTO `boards_auction` VALUES ('458760', 'aden', '34095, 33391', '63', 'false', '', '', '50000000', '2011-06-01 12:00:00', '50000000');
INSERT INTO `boards_auction` VALUES ('458761', 'aden', '34078, 33396', '56', 'false', '', '', '50000000', '2011-06-01 12:00:00', '50000000');
INSERT INTO `boards_auction` VALUES ('458758', 'aden', '34107, 33392', '63', 'false', '', '', '50000000', '2011-06-01 12:00:00', '50000000');
INSERT INTO `boards_auction` VALUES ('458759', 'aden', '34106, 33406', '24', 'false', '', '', '50000000', '2011-06-01 12:00:00', '50000000');
INSERT INTO `boards_auction` VALUES ('458756', 'aden', '34128, 33391', '56', 'false', '', '', '50000000', '2011-06-01 12:00:00', '50000000');
INSERT INTO `boards_auction` VALUES ('458757', 'aden', '34128, 33401', '24', 'false', '', '', '50000000', '2011-06-01 12:00:00', '50000000');
INSERT INTO `boards_auction` VALUES ('458788', 'aden', '33898, 33396', '24', 'false', '', '', '50000000', '2011-06-01 12:00:00', '50000000');
INSERT INTO `boards_auction` VALUES ('458785', 'aden', '33916, 33365', '24', 'false', '', '', '50000000', '2011-06-01 12:00:00', '50000000');
INSERT INTO `boards_auction` VALUES ('458793', 'aden', '33947, 33306', '24', 'false', '', '', '50000000', '2011-06-01 12:00:00', '50000000');
INSERT INTO `boards_auction` VALUES ('458794', 'aden', '33935, 33291', '39', 'false', '', '', '50000000', '2011-06-01 12:00:00', '50000000');
INSERT INTO `boards_auction` VALUES ('458782', 'aden', '33916, 33389', '24', 'false', '', '', '50000000', '2011-06-01 12:00:00', '50000000');
INSERT INTO `boards_auction` VALUES ('458783', 'aden', '33917, 33399', '42', 'false', '', '', '50000000', '2011-06-01 12:00:00', '50000000');
INSERT INTO `boards_auction` VALUES ('458784', 'aden', '33920, 33411', '48', 'false', '', '', '50000000', '2011-06-01 12:00:00', '50000000');
INSERT INTO `boards_auction` VALUES ('262145', 'giran', '33372, 32653', '78', 'false', '', '', '50000000', '2012-08-14 22:22:00', '50000000');
INSERT INTO `boards_auction` VALUES ('262146', 'giran', '33384, 32654', '45', 'false', '', '', '50000000', '2012-08-14 22:22:00', '50000000');
INSERT INTO `boards_auction` VALUES ('262147', 'giran', '33396, 32654', '120', 'false', '', '', '50000000', '2013-08-30 03:25:00', '50000000');
INSERT INTO `boards_auction` VALUES ('262148', 'giran', '33429, 32659', '45', 'false', '', '', '50000000', '2012-08-14 22:22:00', '50000000');
INSERT INTO `boards_auction` VALUES ('262149', 'giran', '33443, 32667', '78', 'false', '', '', '50000000', '2012-08-14 22:22:00', '50000000');
INSERT INTO `boards_auction` VALUES ('262150', 'giran', '33457, 32652', '120', 'false', '', '', '50000000', '2013-08-30 03:25:00', '50000000');
INSERT INTO `boards_auction` VALUES ('262151', 'giran', '33478, 32668', '45', 'false', '', '', '50000000', '2012-08-14 22:22:00', '50000000');
INSERT INTO `boards_auction` VALUES ('262152', 'giran', '33475, 32680', '78', 'false', '', '', '50000000', '2012-08-14 22:22:00', '50000000');
INSERT INTO `boards_auction` VALUES ('262153', 'giran', '33458, 32696', '78', 'false', '', '', '50000000', '2012-08-14 22:22:00', '50000000');
INSERT INTO `boards_auction` VALUES ('262154', 'giran', '33424, 32689', '120', 'false', '', '', '50000000', '2013-08-30 03:25:00', '50000000');
INSERT INTO `boards_auction` VALUES ('262155', 'giran', '33413, 32675', '78', 'false', '', '', '50000000', '2012-08-14 22:22:00', '50000000');
INSERT INTO `boards_auction` VALUES ('262156', 'giran', '33419, 32705', '78', 'false', '', '', '50000000', '2012-08-14 22:22:00', '50000000');
INSERT INTO `boards_auction` VALUES ('262157', 'giran', '33375, 32696', '120', 'false', '', '', '50000000', '2013-08-30 03:25:00', '50000000');
INSERT INTO `boards_auction` VALUES ('262158', 'giran', '33364, 32684', '45', 'false', '', '', '50000000', '2012-08-14 22:22:00', '50000000');
INSERT INTO `boards_auction` VALUES ('262159', 'giran', '33364, 32671', '78', 'true', '', '', '10000000', '2017-08-24 03:46:00', '10000000');
INSERT INTO `boards_auction` VALUES ('262160', 'giran', '33345, 32662', '78', 'true', '', '', '10000000', '2017-08-24 03:46:00', '10000000');
INSERT INTO `boards_auction` VALUES ('262161', 'giran', '33347, 32675', '45', 'true', '', '', '10000000', '2017-08-24 03:46:00', '10000000');
INSERT INTO `boards_auction` VALUES ('262162', 'giran', '33341, 32708', '120', 'true', '', '', '10000000', '2017-08-24 03:46:00', '10000000');
INSERT INTO `boards_auction` VALUES ('262163', 'giran', '33354, 32730', '78', 'true', '', '', '10000000', '2017-08-24 03:46:00', '10000000');
INSERT INTO `boards_auction` VALUES ('262164', 'giran', '33370, 32715', '78', 'true', '', '', '10000000', '2017-08-24 03:46:00', '10000000');
INSERT INTO `boards_auction` VALUES ('262165', 'giran', '33382, 32715', '45', 'true', '', '', '10000000', '2017-08-24 03:46:00', '10000000');
INSERT INTO `boards_auction` VALUES ('262166', 'giran', '33404, 32737', '120', 'true', '', '', '10000000', '2017-08-24 03:46:00', '10000000');
INSERT INTO `boards_auction` VALUES ('262167', 'giran', '33428, 32719', '78', 'true', '', '', '10000000', '2017-08-24 03:46:00', '10000000');
INSERT INTO `boards_auction` VALUES ('262168', 'giran', '33450, 32732', '45', 'true', '', '', '10000000', '2017-08-24 03:46:00', '10000000');
INSERT INTO `boards_auction` VALUES ('262169', 'giran', '33406, 32757', '45', 'true', '', '', '10000000', '2017-08-24 03:46:00', '10000000');
INSERT INTO `boards_auction` VALUES ('262170', 'giran', '33366, 32759', '120', 'true', '', '', '10000000', '2017-08-24 03:46:00', '10000000');
INSERT INTO `boards_auction` VALUES ('262171', 'giran', '33355, 32776', '78', 'true', '', '', '10000000', '2017-08-24 03:46:00', '10000000');
INSERT INTO `boards_auction` VALUES ('262172', 'giran', '33358, 32788', '45', 'true', '', '', '10000000', '2017-08-24 03:46:00', '10000000');
INSERT INTO `boards_auction` VALUES ('262173', 'giran', '33371, 32788', '78', 'true', '', '', '10000000', '2017-08-24 03:46:00', '10000000');
INSERT INTO `boards_auction` VALUES ('262174', 'giran', '33385, 32776', '45', 'true', '', '', '10000000', '2017-08-24 03:46:00', '10000000');
INSERT INTO `boards_auction` VALUES ('262175', 'giran', '33402, 32790', '78', 'true', '', '', '10000000', '2017-08-24 03:46:00', '10000000');
INSERT INTO `boards_auction` VALUES ('262176', 'giran', '33484, 32790', '78', 'true', '', '', '10000000', '2017-08-24 03:46:00', '10000000');
INSERT INTO `boards_auction` VALUES ('262177', 'giran', '33500, 32804', '45', 'true', '', '', '10000000', '2017-08-24 03:46:00', '10000000');
INSERT INTO `boards_auction` VALUES ('262178', 'giran', '33382, 32803', '45', 'true', '', '', '10000000', '2017-08-24 03:46:00', '10000000');
INSERT INTO `boards_auction` VALUES ('262179', 'giran', '33376, 32826', '120', 'true', '', '', '10000000', '2017-08-24 03:46:00', '10000000');
INSERT INTO `boards_auction` VALUES ('262180', 'giran', '33400, 32813', '45', 'true', '', '', '10000000', '2017-08-24 03:46:00', '10000000');
INSERT INTO `boards_auction` VALUES ('262181', 'giran', '33401, 32823', '78', 'true', '', '', '10000000', '2017-08-24 03:46:00', '10000000');
INSERT INTO `boards_auction` VALUES ('262182', 'giran', '33436, 32840', '78', 'true', '', '', '10000000', '2017-08-24 03:46:00', '10000000');
INSERT INTO `boards_auction` VALUES ('262183', 'giran', '33459, 32839', '45', 'true', '', '', '10000000', '2017-08-24 03:46:00', '10000000');
INSERT INTO `boards_auction` VALUES ('262184', 'giran', '33390, 32847', '78', 'true', '', '', '10000000', '2017-08-24 03:46:00', '10000000');
INSERT INTO `boards_auction` VALUES ('262185', 'giran', '33403, 32861', '78', 'true', '', '', '10000000', '2017-08-24 03:46:00', '10000000');
INSERT INTO `boards_auction` VALUES ('262186', 'giran', '33416, 32853', '45', 'true', '', '', '10000000', '2017-08-24 03:46:00', '10000000');
INSERT INTO `boards_auction` VALUES ('262187', 'giran', '33376, 32871', '120', 'true', '', '', '10000000', '2017-08-24 03:46:00', '10000000');
INSERT INTO `boards_auction` VALUES ('262188', 'giran', '33428, 32869', '120', 'true', '', '', '10000000', '2017-08-24 03:46:00', '10000000');
INSERT INTO `boards_auction` VALUES ('262189', 'giran', '33447, 32871', '78', 'true', '', '', '10000000', '2017-08-24 03:46:00', '10000000');
INSERT INTO `boards_auction` VALUES ('327681', 'heine', '33606, 33215', '86', 'false', '', '', '50000000', '2011-06-01 12:00:00', '50000000');
INSERT INTO `boards_auction` VALUES ('327682', 'heine', '33630, 33208', '48', 'false', '', '', '50000000', '2011-06-01 12:00:00', '50000000');
INSERT INTO `boards_auction` VALUES ('327683', 'heine', '33630, 33226', '96', 'false', '', '', '50000000', '2011-06-01 12:00:00', '50000000');
INSERT INTO `boards_auction` VALUES ('327684', 'heine', '33632, 33243', '111', 'false', '', '', '50000000', '2011-06-01 12:00:00', '50000000');
INSERT INTO `boards_auction` VALUES ('327685', 'heine', '33619, 33264', '48', 'false', '', '', '50000000', '2011-06-01 12:00:00', '50000000');
INSERT INTO `boards_auction` VALUES ('327686', 'heine', '33576, 33231', '96', 'false', '', '', '50000000', '2011-06-01 12:00:00', '50000000');
INSERT INTO `boards_auction` VALUES ('327687', 'heine', '33586, 33310', '85', 'false', '', '', '50000000', '2011-06-01 12:00:00', '50000000');
INSERT INTO `boards_auction` VALUES ('327688', 'heine', '33581, 33337', '90', 'false', '', '', '50000000', '2011-06-01 12:00:00', '50000000');
INSERT INTO `boards_auction` VALUES ('327689', 'heine', '33619, 33375', '86', 'false', '', '', '50000000', '2011-06-01 12:00:00', '50000000');
INSERT INTO `boards_auction` VALUES ('327690', 'heine', '33627, 33398', '84', 'false', '', '', '50000000', '2011-06-01 12:00:00', '50000000');
INSERT INTO `boards_auction` VALUES ('327691', 'heine', '33625, 33444', '76', 'false', '', '', '50000000', '2011-06-01 12:00:00', '50000000');
INSERT INTO `boards_auction` VALUES ('458753', 'aden', '34159, 33398', '59', 'false', '', '', '50000000', '2011-06-01 12:00:00', '50000000');
INSERT INTO `boards_auction` VALUES ('458754', 'aden', '34145, 33396', '24', 'false', '', '', '50000000', '2011-06-01 12:00:00', '50000000');
INSERT INTO `boards_auction` VALUES ('458755', 'aden', '34135, 33418', '24', 'false', '', '', '50000000', '2011-06-01 12:00:00', '50000000');
INSERT INTO `boards_auction` VALUES ('458774', 'aden', '33955, 33404', '20', 'false', '', '', '50000000', '2011-06-01 12:00:00', '50000000');
INSERT INTO `boards_auction` VALUES ('458775', 'aden', '34006, 33403', '24', 'false', '', '', '50000000', '2011-06-01 12:00:00', '50000000');
INSERT INTO `boards_auction` VALUES ('458776', 'aden', '33988, 33404', '56', 'false', '', '', '50000000', '2011-06-01 12:00:00', '50000000');
INSERT INTO `boards_auction` VALUES ('458777', 'aden', '33976, 33274', '45', 'false', '', '', '50000000', '2011-06-01 12:00:00', '50000000');
INSERT INTO `boards_auction` VALUES ('458778', 'aden', '33983, 33299', '24', 'false', '', '', '50000000', '2011-06-01 12:00:00', '50000000');
INSERT INTO `boards_auction` VALUES ('458779', 'aden', '33967, 33417', '42', 'false', '', '', '50000000', '2011-06-01 12:00:00', '50000000');
INSERT INTO `boards_auction` VALUES ('458780', 'aden', '33941, 33405', '15', 'false', '', '', '50000000', '2011-06-01 12:00:00', '50000000');
INSERT INTO `boards_auction` VALUES ('458781', 'aden', '33932, 33406', '24', 'false', '', '', '50000000', '2011-06-01 12:00:00', '50000000');
INSERT INTO `boards_auction` VALUES ('458797', 'aden', '33918, 33315', '56', 'false', '', '', '50000000', '2011-06-01 12:00:00', '50000000');
INSERT INTO `boards_auction` VALUES ('458798', 'aden', '33921, 33326', '15', 'false', '', '', '50000000', '2011-06-01 12:00:00', '50000000');
INSERT INTO `boards_auction` VALUES ('458799', 'aden', '33900, 33318', '24', 'false', '', '', '50000000', '2011-06-01 12:00:00', '50000000');
INSERT INTO `boards_auction` VALUES ('458815', 'aden', '34055, 33109', '63', 'false', '', '', '50000000', '2011-06-01 12:00:00', '50000000');
INSERT INTO `boards_auction` VALUES ('458816', 'aden', '34044, 33107', '37', 'false', '', '', '50000000', '2011-06-01 12:00:00', '50000000');
INSERT INTO `boards_auction` VALUES ('458817', 'aden', '34029, 33109', '24', 'false', '', '', '50000000', '2011-06-01 12:00:00', '50000000');
INSERT INTO `boards_auction` VALUES ('458818', 'aden', '34041, 33130', '48', 'false', '', '', '50000000', '2011-06-01 12:00:00', '50000000');
INSERT INTO `boards_auction` VALUES ('458819', 'aden', '34030, 33130', '24', 'false', '', '', '50000000', '2011-06-01 12:00:00', '50000000');

-- ----------------------------
-- Table structure for `clan_agit`
-- ----------------------------
DROP TABLE IF EXISTS `clan_agit`;
CREATE TABLE `clan_agit` (
  `uid` int(10) unsigned NOT NULL DEFAULT '0',
  `cha_objId` int(10) NOT NULL,
  `cha_name` varchar(20) NOT NULL DEFAULT '',
  `clan_id` int(10) unsigned NOT NULL DEFAULT '0',
  `clan_name` varchar(50) NOT NULL DEFAULT '',
  `agit_name` varchar(50) NOT NULL DEFAULT '',
  `agit_x` int(10) NOT NULL DEFAULT '0',
  `agit_y` int(10) NOT NULL DEFAULT '0',
  `agit_map` int(10) NOT NULL DEFAULT '0',
  `agit_door` varchar(255) NOT NULL DEFAULT '',
  `agit_sign` varchar(255) NOT NULL DEFAULT '',
  `agit_npc` varchar(255) NOT NULL DEFAULT '',
  PRIMARY KEY (`uid`),
  KEY `uid` (`uid`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of clan_agit
-- ----------------------------
INSERT INTO `clan_agit` VALUES ('262145', '0', '', '0', '', '1', '33372', '32653', '4', '33373,32657|', '33368,32656', '33373,32656|');
INSERT INTO `clan_agit` VALUES ('262146', '0', '', '0', '', '2', '33384', '32654', '4', '33384,32656|', '33381,32657', '33385,32656|');
INSERT INTO `clan_agit` VALUES ('262147', '0', '', '0', '', '3', '33396', '32654', '4', '33395,32657|33394,32649|', '33398,32658', '33397,32657|');
INSERT INTO `clan_agit` VALUES ('262148', '0', '', '0', '', '4', '33429', '32659', '4', '33427,32659|', '33426,32662', '33427,32658|');
INSERT INTO `clan_agit` VALUES ('262149', '0', '', '0', '', '5', '33443', '32667', '4', '33439,32667|', '33440,32671', '33440,32667|');
INSERT INTO `clan_agit` VALUES ('262150', '0', '', '0', '', '6', '33457', '32652', '4', '33456,32647|33457,32655|', '33460,32656', '33458,32655|');
INSERT INTO `clan_agit` VALUES ('262151', '0', '', '0', '', '7', '33478', '32668', '4', '33476,32668|', '33475,32671', '33476,32667|');
INSERT INTO `clan_agit` VALUES ('262152', '0', '', '0', '', '8', '33475', '32680', '4', '33471,32680|', '33472,32684', '33472,32680|');
INSERT INTO `clan_agit` VALUES ('262153', '0', '', '0', '', '9', '33458', '32696', '4', '33458,32700|', '33454,32699', '33458,32699|');
INSERT INTO `clan_agit` VALUES ('262154', '0', '', '0', '', '10', '33424', '32689', '4', '33423,32684|33424,32692|', '33427,32693', '33425,32692|');
INSERT INTO `clan_agit` VALUES ('262155', '0', '', '0', '', '11', '33413', '32675', '4', '33409,32676|', '33410,32680', '33410,32676|');
INSERT INTO `clan_agit` VALUES ('262156', '0', '', '0', '', '12', '33419', '32705', '4', '33419,32709|', '33415,32708', '33419,32708|');
INSERT INTO `clan_agit` VALUES ('262157', '0', '', '0', '', '13', '33375', '32696', '4', '33375,32699|33374,32691|', '33378,32700', '33376,32699|');
INSERT INTO `clan_agit` VALUES ('262158', '0', '', '0', '', '14', '33364', '32684', '4', '33362,32684|', '33361,32687', '33362,32683|');
INSERT INTO `clan_agit` VALUES ('262159', '0', '', '0', '', '15', '33364', '32671', '4', '33360,32671|', '33361,32675', '33361,32671|');
INSERT INTO `clan_agit` VALUES ('262160', '0', '', '0', '', '16', '33345', '32662', '4', '33341,32662|', '33342,32666', '33342,32662|');
INSERT INTO `clan_agit` VALUES ('262161', '0', '', '0', '', '17', '33347', '32675', '4', '33345,32675|', '33344,32678', '33345,32674|');
INSERT INTO `clan_agit` VALUES ('262162', '0', '', '0', '', '18', '33341', '32708', '4', '33341,32711|33340,32703|', '33344,32712', '33342,32711|');
INSERT INTO `clan_agit` VALUES ('262163', '0', '', '0', '', '19', '33354', '32730', '4', '33354,32734|', '33350,32733', '33354,32733|');
INSERT INTO `clan_agit` VALUES ('262164', '0', '', '0', '', '20', '33370', '32715', '4', '33366,32715|', '33367,32719', '33367,32715|');
INSERT INTO `clan_agit` VALUES ('262165', '0', '', '0', '', '21', '33382', '32715', '4', '33380,32715|', '33379,32718', '33380,32714|');
INSERT INTO `clan_agit` VALUES ('262166', '0', '', '0', '', '22', '33404', '32737', '4', '33404,32740|33403,32732|', '33407,32741', '33405,32740|');
INSERT INTO `clan_agit` VALUES ('262167', '0', '', '0', '', '23', '33428', '32719', '4', '33424,32719|', '33425,32723', '33425,32719|');
INSERT INTO `clan_agit` VALUES ('262168', '0', '', '0', '', '24', '33450', '32732', '4', '33448,32732|', '33447,32735', '33448,32731|');
INSERT INTO `clan_agit` VALUES ('262169', '0', '', '0', '', '25', '33406', '32757', '4', '33404,32757|', '33403,32760', '33404,32756|');
INSERT INTO `clan_agit` VALUES ('262170', '0', '', '0', '', '26', '33366', '32759', '4', '33366,32762|33365,32754|', '33369,32763', '');
INSERT INTO `clan_agit` VALUES ('262171', '0', '', '0', '', '27', '33355', '32776', '4', '33351,32776|', '33352,32780', '33352,32776|');
INSERT INTO `clan_agit` VALUES ('262172', '0', '', '0', '', '28', '33358', '32788', '4', '33358,32790|', '33355,32791', '33359,32790|');
INSERT INTO `clan_agit` VALUES ('262173', '0', '', '0', '', '29', '33371', '32788', '4', '33371,32792|', '33367,32791', '');
INSERT INTO `clan_agit` VALUES ('262174', '0', '', '0', '', '30', '33385', '32776', '4', '33383,32776|', '33382,32779', '33383,32775|');
INSERT INTO `clan_agit` VALUES ('262175', '0', '', '0', '', '31', '33402', '32790', '4', '33402,32794|', '33398,32793', '33402,32793|');
INSERT INTO `clan_agit` VALUES ('262176', '0', '', '0', '', '32', '33484', '32790', '4', '33484,32794|', '33480,32793', '33484,32793|');
INSERT INTO `clan_agit` VALUES ('262177', '0', '', '0', '', '33', '33500', '32804', '4', '33498,32804|', '33497,32807', '33498,32803|');
INSERT INTO `clan_agit` VALUES ('262178', '0', '', '0', '', '34', '33382', '32803', '4', '33382,32805|', '33379,32806', '');
INSERT INTO `clan_agit` VALUES ('262179', '0', '', '0', '', '35', '33376', '32826', '4', '33376,32829|33375,32821|', '33378,32830', '33377,32829|');
INSERT INTO `clan_agit` VALUES ('262180', '0', '', '0', '', '36', '33400', '32813', '4', '33398,32813|', '33397,32816', '33398,32812|');
INSERT INTO `clan_agit` VALUES ('262181', '0', '', '0', '', '37', '33401', '32823', '4', '33397,32823|', '33398,32827', '33398,32823|');
INSERT INTO `clan_agit` VALUES ('262182', '0', '', '0', '', '38', '33436', '32840', '4', '33436,32844|', '33432,32843', '33436,32843|');
INSERT INTO `clan_agit` VALUES ('262183', '0', '', '0', '', '39', '33460', '32833', '4', '33460,32835|', '33462,32836', '');
INSERT INTO `clan_agit` VALUES ('262184', '0', '', '0', '', '40', '33390', '32847', '4', '33390,32851|', '33386,32850', '');
INSERT INTO `clan_agit` VALUES ('262185', '0', '', '0', '', '41', '33403', '32861', '4', '33399,32861|', '33400,32864', '');
INSERT INTO `clan_agit` VALUES ('262186', '0', '', '0', '', '42', '33416', '32853', '4', '33414,32853|', '33413,32856', '');
INSERT INTO `clan_agit` VALUES ('262187', '0', '', '0', '', '43', '33376', '32871', '4', '33375,32874|33374,32866|', '33378,32875', '33376,32874|');
INSERT INTO `clan_agit` VALUES ('262188', '0', '', '0', '', '44', '33428', '32869', '4', '33428,32872|33427,32864|', '33431,32873', '33429,32872|');
INSERT INTO `clan_agit` VALUES ('262189', '0', '', '0', '', '45', '33447', '32871', '4', '33443,32871|', '33444,32875', '33444,32871|');
INSERT INTO `clan_agit` VALUES ('327681', '0', '', '0', '', '1', '33606', '33215', '4', '33609,33218|33601,33217|', '33599,33223', '33610,33218|');
INSERT INTO `clan_agit` VALUES ('327682', '0', '', '0', '', '2', '33630', '33208', '4', '33630,33210|', '33632,33214', '33629,33210|');
INSERT INTO `clan_agit` VALUES ('327683', '0', '', '0', '', '3', '33630', '33226', '4', '33630,33231|33626,33226|', '33624,33231', '33626,33225|');
INSERT INTO `clan_agit` VALUES ('327684', '0', '', '0', '', '4', '33632', '33243', '4', '33632,33248|', '33626,33251', '33632,33247|');
INSERT INTO `clan_agit` VALUES ('327685', '0', '', '0', '', '5', '33619', '33264', '4', '33619,33266|', '33614,33270', '33618,33266|');
INSERT INTO `clan_agit` VALUES ('327686', '0', '', '0', '', '6', '33576', '33231', '4', '33575,33234|33570,33230|', '33582,33236', '33367,32715|');
INSERT INTO `clan_agit` VALUES ('327687', '0', '', '0', '', '7', '33586', '33310', '4', '33583,33306|33584,33314|', '33575,33305', '33583,33305|');
INSERT INTO `clan_agit` VALUES ('327688', '0', '', '0', '', '8', '33581', '33337', '4', '33581,33339|', '33584,33346', '33582,33339|');
INSERT INTO `clan_agit` VALUES ('327689', '0', '', '0', '', '9', '33619', '33375', '4', '33619,33381|', '33613,33384', '');
INSERT INTO `clan_agit` VALUES ('327690', '0', '', '0', '', '10', '33627', '33398', '4', '33628,33403|33624,33398|', '33622,33404', '');
INSERT INTO `clan_agit` VALUES ('327691', '0', '', '0', '', '11', '33625', '33444', '4', '33625,33446|', '33621,33450', '');
INSERT INTO `clan_agit` VALUES ('458753', '0', '', '0', '', '1', '34159', '33398', '4', '34158,33404|34155,33400|34158,33395|', '34154,33404', '34159,33403|');
INSERT INTO `clan_agit` VALUES ('458754', '0', '', '0', '', '2', '34145', '33396', '4', '34143,33398|34146,33398|', '', '');
INSERT INTO `clan_agit` VALUES ('458755', '0', '', '0', '', '3', '34135', '33418', '4', '34131,33418|34139,33418|', '34130,33420', '34132,33419|');
INSERT INTO `clan_agit` VALUES ('458756', '0', '', '0', '', '4', '34128', '33391', '4', '34124,33391|34128,33387|34128,33394|', '34123,33394', '34125,33392|');
INSERT INTO `clan_agit` VALUES ('458757', '0', '', '0', '', '5', '34128', '33401', '4', '34124,33401|34132,33401|', '34123,33403', '34125,33402|');
INSERT INTO `clan_agit` VALUES ('458758', '0', '', '0', '', '6', '34107', '33392', '4', '34104,33392|34107,33387|34111,33392|', '34104,33394', '33610,33218|');
INSERT INTO `clan_agit` VALUES ('458759', '0', '', '0', '', '7', '34106', '33406', '4', '34106,33409|34106,33401|', '34109,33410', '34105,33408|');
INSERT INTO `clan_agit` VALUES ('458760', '0', '', '0', '', '8', '34095', '33391', '4', '34091,33391|34095,33394|34095,33387|34100,33391|', '34091,33395', '33525,32875|');
INSERT INTO `clan_agit` VALUES ('458761', '0', '', '0', '', '9', '34078', '33396', '4', '34077,33392|34082,33396|34077,33399|', '', '');
INSERT INTO `clan_agit` VALUES ('458762', '0', '', '0', '', '10', '34064', '33397', '4', '', '', '');
INSERT INTO `clan_agit` VALUES ('458763', '0', '', '0', '', '11', '34147', '33370', '4', '34147,33373|34147,33365|', '34150,33375', '');
INSERT INTO `clan_agit` VALUES ('458764', '0', '', '0', '', '12', '34074', '33356', '4', '34074,33359|34079,33356|34074,33352|34070,33356|', '34071,33360', '');
INSERT INTO `clan_agit` VALUES ('458765', '0', '', '0', '', '13', '34128', '33354', '4', '34124,33354|34132,33354|', '34123,33357', '');
INSERT INTO `clan_agit` VALUES ('458766', '0', '', '0', '', '14', '34127', '33368', '4', '34127,33372|34124,33368|', '34130,33373', '');
INSERT INTO `clan_agit` VALUES ('458767', '0', '', '0', '', '15', '34091', '33350', '4', '34091,33353|34091,33345|', '34089,33354', '');
INSERT INTO `clan_agit` VALUES ('458768', '0', '', '0', '', '16', '34098', '33370', '4', '34098,33373|34098,33366|34094,33370|', '34093,33367', '');
INSERT INTO `clan_agit` VALUES ('458769', '0', '', '0', '', '17', '34061', '33376', '4', '34061,33380|34058,33376|34061,33371|34065,33376|', '34063,33381', '');
INSERT INTO `clan_agit` VALUES ('458770', '0', '', '0', '', '18', '34075', '33382', '4', '34072,33383|34072,33380|', '34070,33380', '');
INSERT INTO `clan_agit` VALUES ('458771', '0', '', '0', '', '19', '34078', '33370', '4', '34077,33365|34077,33373|', '34073,33373', '');
INSERT INTO `clan_agit` VALUES ('458772', '0', '', '0', '', '20', '34027', '33382', '4', '34028,33385|34022,33384|34022,33382|', '', '');
INSERT INTO `clan_agit` VALUES ('458773', '0', '', '0', '', '21', '34015', '33374', '4', '', '', '');
INSERT INTO `clan_agit` VALUES ('458774', '0', '', '0', '', '22', '33955', '33404', '4', '', '', '');
INSERT INTO `clan_agit` VALUES ('458775', '0', '', '0', '', '23', '34006', '33403', '4', '34002,33403|', '34001,33401', '');
INSERT INTO `clan_agit` VALUES ('458776', '0', '', '0', '', '24', '33988', '33404', '4', '33987,33400|33992,33404|', '33993,33406', '');
INSERT INTO `clan_agit` VALUES ('458777', '0', '', '0', '', '25', '33976', '33274', '4', '33972,33274|', '33971,33276', '');
INSERT INTO `clan_agit` VALUES ('458778', '0', '', '0', '', '26', '33983', '33299', '4', '33983,33300|', '33981,33302', '');
INSERT INTO `clan_agit` VALUES ('458779', '0', '', '0', '', '27', '33967', '33417', '4', '33967,33413|', '', '');
INSERT INTO `clan_agit` VALUES ('458780', '0', '', '0', '', '28', '33941', '33405', '4', '33940,33405|', '33938,33408', '');
INSERT INTO `clan_agit` VALUES ('458781', '0', '', '0', '', '29', '33932', '33406', '4', '33932,33409|', '33929,33409', '');
INSERT INTO `clan_agit` VALUES ('458782', '0', '', '0', '', '30', '33916', '33389', '4', '33912,33389|33920,33389|', '33911,33391', '');
INSERT INTO `clan_agit` VALUES ('458783', '0', '', '0', '', '31', '33917', '33399', '4', '33917,33395|', '', '');
INSERT INTO `clan_agit` VALUES ('458784', '0', '', '0', '', '32', '33920', '33411', '4', '33923,33411|33919,33406|', '33915,33407', '');
INSERT INTO `clan_agit` VALUES ('458785', '0', '', '0', '', '33', '33916', '33365', '4', '33920,33365|33912,33365|', '33911,33363', '');
INSERT INTO `clan_agit` VALUES ('458786', '0', '', '0', '', '34', '33917', '33372', '4', '33917,33373|', '33915,33376', '');
INSERT INTO `clan_agit` VALUES ('458787', '0', '', '0', '', '35', '34001', '33324', '4', '34001,33328|', '33999,33329', '');
INSERT INTO `clan_agit` VALUES ('458788', '0', '', '0', '', '36', '33898', '33396', '4', '33902,33396|33894,33396|', '33893,33394', '');
INSERT INTO `clan_agit` VALUES ('458789', '0', '', '0', '', '37', '33887', '33381', '4', '33883,33381|33883,33383|33889,33384|', '33891,33386', '');
INSERT INTO `clan_agit` VALUES ('458790', '0', '', '0', '', '38', '33921', '33302', '4', '33921,33303|', '33920,33305', '');
INSERT INTO `clan_agit` VALUES ('458791', '0', '', '0', '', '39', '33957', '33324', '4', '33956,33324|', '33954,33326', '');
INSERT INTO `clan_agit` VALUES ('458792', '0', '', '0', '', '40', '33898', '33218', '4', '33895,33216|33895,33219|', '33894,33220', '');
INSERT INTO `clan_agit` VALUES ('458793', '0', '', '0', '', '41', '33947', '33306', '4', '33947,33309|33947,33301|', '33950,33310', '');
INSERT INTO `clan_agit` VALUES ('458794', '0', '', '0', '', '42', '33935', '33291', '4', '33932,33293|33932,33291|33934,33296|', '33932,33298', '');
INSERT INTO `clan_agit` VALUES ('458795', '0', '', '0', '', '43', '33921', '33293', '4', '33921,33296|', '33918,33297', '');
INSERT INTO `clan_agit` VALUES ('458796', '0', '', '0', '', '44', '33912', '33291', '4', '33911,33291|', '33908,33293', '');
INSERT INTO `clan_agit` VALUES ('458797', '0', '', '0', '', '45', '33918', '33315', '4', '33918,33318|33918,33311|33914,33315|', '33920,33319', '');
INSERT INTO `clan_agit` VALUES ('458798', '0', '', '0', '', '46', '33921', '33326', '4', '33921,33327|', '33919,33329', '');
INSERT INTO `clan_agit` VALUES ('458799', '0', '', '0', '', '47', '33900', '33318', '4', '33896,33318|', '33895,33320', '');
INSERT INTO `clan_agit` VALUES ('458800', '0', '', '0', '', '48', '33905', '33329', '4', '33905,33324|33905,33332|', '33903,33333', '');
INSERT INTO `clan_agit` VALUES ('458801', '0', '', '0', '', '49', '33907', '33350', '4', '33911,33350|33903,33350|', '33902,33352', '');
INSERT INTO `clan_agit` VALUES ('458802', '0', '', '0', '', '50', '33892', '33354', '4', '33895,33354|33891,33349|', '33889,33348', '');
INSERT INTO `clan_agit` VALUES ('458803', '0', '', '0', '', '51', '33976', '33204', '4', '33973,33203|33973,33206|', '33972,33207', '');
INSERT INTO `clan_agit` VALUES ('458804', '0', '', '0', '', '52', '33919', '33230', '4', '33923,33230|33915,33230|', '33914,33231', '');
INSERT INTO `clan_agit` VALUES ('458805', '0', '', '0', '', '53', '33933', '33210', '4', '33932,33205|33932,33214|', '33934,33215', '');
INSERT INTO `clan_agit` VALUES ('458806', '0', '', '0', '', '54', '33916', '33193', '4', '33916,33188|33916,33196|', '33918,33197', '');
INSERT INTO `clan_agit` VALUES ('458807', '0', '', '0', '', '55', '34157', '33141', '4', '34157,33145|34157,33136|', '34155,33146', '');
INSERT INTO `clan_agit` VALUES ('458808', '0', '', '0', '', '56', '34144', '33143', '4', '34149,33143|34144,33146|34140,33143|34144,33139|', '34139,33145', '');
INSERT INTO `clan_agit` VALUES ('458809', '0', '', '0', '', '57', '34129', '33114', '4', '34129,33110|', '34126,33109', '');
INSERT INTO `clan_agit` VALUES ('458810', '0', '', '0', '', '58', '34122', '33163', '4', '34127,33163|34122,33159|34118,33163|', '34117,33165', '');
INSERT INTO `clan_agit` VALUES ('458811', '0', '', '0', '', '59', '34127', '33129', '4', '34127,33124|34127,33132|', '34125,33133', '');
INSERT INTO `clan_agit` VALUES ('458812', '0', '', '0', '', '60', '34099', '33110', '4', '34099,33105|34103,33110|34096,33110|34099,33114|', '34102,33115', '');
INSERT INTO `clan_agit` VALUES ('458813', '0', '', '0', '', '61', '34108', '33121', '4', '34105,33122|34105,33119|', '34104,33124', '');
INSERT INTO `clan_agit` VALUES ('458814', '0', '', '0', '', '62', '34144', '33167', '4', '34148,33167|34140,33167|', '34139,33168', '');
INSERT INTO `clan_agit` VALUES ('458815', '0', '', '0', '', '63', '34055', '33109', '4', '34060,33109|34055,33105|34051,33109|34055,33112|', '34057,33113', '');
INSERT INTO `clan_agit` VALUES ('458816', '0', '', '0', '', '64', '34044', '33107', '4', '34041,33108|34041,33106|34043,33111|', '34045,33113', '');
INSERT INTO `clan_agit` VALUES ('458817', '0', '', '0', '', '65', '34029', '33109', '4', '34029,33104|34029,33112|', '34027,33112', '');
INSERT INTO `clan_agit` VALUES ('458818', '0', '', '0', '', '66', '34041', '33130', '4', '34044,33129|34040,33133|', '34038,33134', '');
INSERT INTO `clan_agit` VALUES ('458819', '0', '', '0', '', '67', '34030', '33130', '4', '34030,33133|', '34028,33134', '');

-- ----------------------------
-- Table structure for `hack_no_check_ip`
-- ----------------------------
DROP TABLE IF EXISTS `hack_no_check_ip`;
CREATE TABLE `hack_no_check_ip` (
  `ip` varchar(255) NOT NULL DEFAULT '',
  PRIMARY KEY (`ip`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of hack_no_check_ip
-- ----------------------------

-- ----------------------------
-- Table structure for `enchant_lost_item`
-- ----------------------------
DROP TABLE IF EXISTS `enchant_lost_item`;
CREATE TABLE `enchant_lost_item` (
  `캐릭터_objId` bigint(20) NOT NULL DEFAULT '0',
  `캐릭터` varchar(255) NOT NULL DEFAULT '',
  `아이템_objId` bigint(20) NOT NULL DEFAULT '0',
  `아이템` varchar(255) NOT NULL DEFAULT '',
  `인첸트` int(10) NOT NULL DEFAULT '0',
  `축복` int(10) NOT NULL DEFAULT '1',
  `수량` bigint(20) NOT NULL DEFAULT '1',
  `잃은시간` datetime NOT NULL DEFAULT '0000-00-00 00:00:00',
  `주문서` varchar(255) NOT NULL DEFAULT '',
  `주문서_축복` int(10) NOT NULL DEFAULT '1',
  `지급여부` int(10) NOT NULL DEFAULT '0',
  PRIMARY KEY (`캐릭터_objId`,`아이템_objId`,`지급여부`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of enchant_lost_item
-- ----------------------------

-- ----------------------------
-- Table structure for `member`
-- ----------------------------
DROP TABLE IF EXISTS `member`;
CREATE TABLE `member` (
  `계정` varchar(255) NOT NULL DEFAULT '',
  `캐릭터` varchar(255) NOT NULL DEFAULT '',
  `연락처` varchar(255) NOT NULL DEFAULT '',
  `고정신청날짜` datetime NOT NULL DEFAULT '0000-00-00 00:00:00',
  PRIMARY KEY (`계정`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of member
-- ----------------------------

-- ----------------------------
-- Table structure for `character_marble`
-- ----------------------------
DROP TABLE IF EXISTS `character_marble`;
CREATE TABLE `character_marble` (
  `item_objId` bigint(20) NOT NULL DEFAULT '0',
  `cha_objId` bigint(20) NOT NULL DEFAULT '0',
  `캐릭터` varchar(255) NOT NULL DEFAULT '',
  `레벨` tinyint(3) NOT NULL DEFAULT '0',
  `경험치` varchar(255) NOT NULL DEFAULT '',
  `클래스` tinyint(3) NOT NULL DEFAULT '0',
  `성별` tinyint(3) NOT NULL DEFAULT '0',
  `저장날짜` datetime NOT NULL DEFAULT '0000-00-00 00:00:00',
  PRIMARY KEY (`item_objId`,`cha_objId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of character_marble
-- ----------------------------