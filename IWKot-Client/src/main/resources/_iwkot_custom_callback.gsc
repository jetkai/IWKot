#include maps\mp\_utility;
#include maps\mp\gametypes\_hud_util;
#include common_scripts\utility;

init() {
	SetDvarIfUninitialized("sv_customcallbacks", true);

	level thread onPlayerConnect();
	
	level waittill("prematch_over");
	level.callbackPlayerDisconnect = ::onPlayerDisconnect;
}

//Send PlayerJoin request to IWKot-Server
onPlayerConnect(player) {
	for(;;) {
		level waittill("connected", player);	
		//::Callback_PlayerConnect()
	}
}

//Send PlayerLeave request to IWKot-Server
onPlayerDisconnect() {
	level notify("disconnected", self);
	self maps\mp\gametypes\_playerlogic::Callback_PlayerDisconnect();
}

//TODO
output() { }
