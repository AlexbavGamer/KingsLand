package KingsLand.server.classmanager.classes;

import KingsLand.server.classmanager.FileManager;

public class Config extends FileManager
{
	public Config(String FileName)
	{
		super(FileName);
	}

	@Override
	public void SetupFile()
	{
		SetValue("config.BetPriceMin", 10000);
		SetValue("config.BetPriceMax", 50000);
		SetValue("config.prefix", "&2[&4PvP&2]&r");
		SetValue("DisabledCommand", "%prefix% O Comando /%cmd% está desabilitado !");
		SetValue("NoPermission", "&2Sorry. Você não tem permissão para usar &3KingsLand.%cmd%.");
		SetValue("commands.acceptCmd", "aceitar");
		SetValue("commands.denyCmd", "recusar");
		SetValue("commands.cancelCmd", "cancelar");
		SetValue("commands.inviteCmd", "duelar");
		SetValue("commands.saveinv", "salvarinv");
		SetValue("commands.loadinv", "carregarinv");
		SetValue("InvalidParam","%prefix% Não é possivel achar o resultado de {0}");
		
		//Messages of Player of /pvp command
		SetValue("config.messages.saveinv.usage", "%prefix% use: /%cmd% %saveinv% [Player]");
		SetValue("config.messages.loadinv.usage", "%prefix% use: /%cmd% %loadinv% [Player]");
		
		SetValue("config.messages.saveinv.error", "%prefix% [Failed to Save] - Jogador {1} não esta online.");
		SetValue("config.messages.loadinv.error", "%prefix% [Failed to Load] - Jogador {1} não esta online.");
		
		SetValue("config.messages.loadinv.success", "%prefix% [Save] - Inventorio do Jogador {1} foi carregado.");
		SetValue("config.messages.saveinv.success", "%prefix% [Save] - Inventorio do Jogador {1} foi salvo.");
		
		SetValue("config.messages.accept.error","%prefix% Somente %target% podera aceitar o convite de duelo.");
		SetValue("config.messages.accept.accepted", "%prefix% Você aceitou o pedido de duelo de %sender%.");
		
		SetValue("config.messages.deny.deny", "%prefix% você recusou o pedido de duelo de %target%");
		SetValue("config.messages.deny.denyed","%prefix% %target% recusou o seu pedido de duelo");
		SetValue("config.messages.deny.error","%prefix% você não tem nenhum convite de duelo para recusar.");
		SetValue("config.messages.accept.acceptedtarget", "%prefix% %target% aceitou o seu pedido de duelo.");
		SetValue("config.messages.accept.noconvited", "%prefix% você não tem nenhum convite de duelo para aceitar.");
		
		SetValue("config.messages.invite.send.1", "%prefix% -> &cPedido de &6Duelo&c Recebido");
		SetValue("config.messages.invite.send.2", "%prefix% -> &cDesafiante: %player%");
		SetValue("config.messages.invite.send.3", "%prefix% -> &cAposta: %bet%");
		SetValue("config.messages.invite.send.4", "%prefix% -> &cUse: /%cmd% %accept% para aceitar o pedido");
		SetValue("config.messages.invite.send.5", "%prefix% -> &cUse: /%cmd% %deny% para recusar o pedido");
		SetValue("config.messages.invite.send.6", "%prefix% -> &cPedido de Duelo Recebido");
		SetValue("config.messages.invite.send.7", "%prefix% -> &6Pedido de duelo enviado a %target%.");
		
		save();
	}
}
