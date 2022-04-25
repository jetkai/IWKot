#include maps\mp\_utility;
#include maps\mp\gametypes\_hud_util;
#include common_scripts\utility;

init() {
	SetDvarIfUninitialized("sv_customcallbacks", true);

	level thread onPlayerConnect();
	
	level waittill("prematch_over");
	//::Callback_PlayerConnect()
	//level.callbackPlayerDisconnect = ::onPlayerDisconnect;
}

//Send PlayerJoin request to IWKot-Server
onPlayerConnect(player) {
	for(;;) {
		level waittill("connected", player);

		//bearer = getDvar( "bearer_token");
		//hash = getDvar( "maphash");

		mapName = getDvar("mapname");
		port = getDvar("net_port");
		//if(isSubstr(player.guid, "bot") || player.pers["isBot"]) //Return if is a Bot
		//	return
		logPrint( "++join::mapName=" + mapName + ",name=" + player.name + ",guid=" + player.guid +",port=" + port +"\n");
	}
}

//Send PlayerLeave request to IWKot-Server
onPlayerDisconnect() {
	level notify("disconnected", self);
	self maps\mp\gametypes\_playerlogic::Callback_PlayerDisconnect();
}

//TODO
output() { }
