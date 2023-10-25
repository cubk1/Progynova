package net.minecraft.network;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.mojang.authlib.GameProfile;
import java.lang.reflect.Type;
import java.util.UUID;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.JsonUtils;

public class ServerStatusResponse
{
    private IChatComponent serverMotd;
    private ServerStatusResponse.PlayerCountData playerCount;
    private ServerStatusResponse.MinecraftProtocolVersionIdentifier protocolVersion;
    private String favicon;

    public IChatComponent getServerDescription()
    {
        return this.serverMotd;
    }

    public void setServerDescription(IChatComponent motd)
    {
        this.serverMotd = motd;
    }

    public ServerStatusResponse.PlayerCountData getPlayerCountData()
    {
        return this.playerCount;
    }

    public void setPlayerCountData(ServerStatusResponse.PlayerCountData countData)
    {
        this.playerCount = countData;
    }

    public ServerStatusResponse.MinecraftProtocolVersionIdentifier getProtocolVersionInfo()
    {
        return this.protocolVersion;
    }

    public void setProtocolVersionInfo(ServerStatusResponse.MinecraftProtocolVersionIdentifier protocolVersionData)
    {
        this.protocolVersion = protocolVersionData;
    }

    public void setFavicon(String faviconBlob)
    {
        this.favicon = faviconBlob;
    }

    public String getFavicon()
    {
        return this.favicon;
    }

    public static class MinecraftProtocolVersionIdentifier
    {
        private final String name;
        private final int protocol;

        public MinecraftProtocolVersionIdentifier(String nameIn, int protocolIn)
        {
            this.name = nameIn;
            this.protocol = protocolIn;
        }

        public String getName()
        {
            return this.name;
        }

        public int getProtocol()
        {
            return this.protocol;
        }

        public static class Serializer implements JsonDeserializer<ServerStatusResponse.MinecraftProtocolVersionIdentifier>, JsonSerializer<ServerStatusResponse.MinecraftProtocolVersionIdentifier>
        {
            public ServerStatusResponse.MinecraftProtocolVersionIdentifier deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException
            {
                JsonObject jsonobject = JsonUtils.getJsonObject(jsonElement, "version");
                return new ServerStatusResponse.MinecraftProtocolVersionIdentifier(JsonUtils.getString(jsonobject, "name"), JsonUtils.getInt(jsonobject, "protocol"));
            }

            public JsonElement serialize(ServerStatusResponse.MinecraftProtocolVersionIdentifier minecraftProtocolVersionIdentifier, Type type, JsonSerializationContext jsonSerializationContext)
            {
                JsonObject jsonobject = new JsonObject();
                jsonobject.addProperty("name", minecraftProtocolVersionIdentifier.getName());
                jsonobject.addProperty("protocol", minecraftProtocolVersionIdentifier.getProtocol());
                return jsonobject;
            }
        }
    }

    public static class PlayerCountData
    {
        private final int maxPlayers;
        private final int onlinePlayerCount;
        private GameProfile[] players;

        public PlayerCountData(int maxOnlinePlayers, int onlinePlayers)
        {
            this.maxPlayers = maxOnlinePlayers;
            this.onlinePlayerCount = onlinePlayers;
        }

        public int getMaxPlayers()
        {
            return this.maxPlayers;
        }

        public int getOnlinePlayerCount()
        {
            return this.onlinePlayerCount;
        }

        public GameProfile[] getPlayers()
        {
            return this.players;
        }

        public void setPlayers(GameProfile[] playersIn)
        {
            this.players = playersIn;
        }

        public static class Serializer implements JsonDeserializer<ServerStatusResponse.PlayerCountData>, JsonSerializer<ServerStatusResponse.PlayerCountData>
        {
            public ServerStatusResponse.PlayerCountData deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException
            {
                JsonObject jsonobject = JsonUtils.getJsonObject(jsonElement, "players");
                ServerStatusResponse.PlayerCountData serverstatusresponse$playercountdata = new ServerStatusResponse.PlayerCountData(JsonUtils.getInt(jsonobject, "max"), JsonUtils.getInt(jsonobject, "online"));

                if (JsonUtils.isJsonArray(jsonobject, "sample"))
                {
                    JsonArray jsonarray = JsonUtils.getJsonArray(jsonobject, "sample");

                    if (jsonarray.size() > 0)
                    {
                        GameProfile[] agameprofile = new GameProfile[jsonarray.size()];

                        for (int i = 0; i < agameprofile.length; ++i)
                        {
                            JsonObject jsonobject1 = JsonUtils.getJsonObject(jsonarray.get(i), "player[" + i + "]");
                            String s = JsonUtils.getString(jsonobject1, "id");
                            agameprofile[i] = new GameProfile(UUID.fromString(s), JsonUtils.getString(jsonobject1, "name"));
                        }

                        serverstatusresponse$playercountdata.setPlayers(agameprofile);
                    }
                }

                return serverstatusresponse$playercountdata;
            }

            public JsonElement serialize(ServerStatusResponse.PlayerCountData countData, Type type, JsonSerializationContext jsonSerializationContext)
            {
                JsonObject jsonobject = new JsonObject();
                jsonobject.addProperty("max", countData.getMaxPlayers());
                jsonobject.addProperty("online", countData.getOnlinePlayerCount());

                if (countData.getPlayers() != null && countData.getPlayers().length > 0)
                {
                    JsonArray jsonarray = new JsonArray();

                    for (int i = 0; i < countData.getPlayers().length; ++i)
                    {
                        JsonObject jsonobject1 = new JsonObject();
                        UUID uuid = countData.getPlayers()[i].getId();
                        jsonobject1.addProperty("id", uuid == null ? "" : uuid.toString());
                        jsonobject1.addProperty("name", countData.getPlayers()[i].getName());
                        jsonarray.add(jsonobject1);
                    }

                    jsonobject.add("sample", jsonarray);
                }

                return jsonobject;
            }
        }
    }

    public static class Serializer implements JsonDeserializer<ServerStatusResponse>, JsonSerializer<ServerStatusResponse>
    {
        public ServerStatusResponse deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException
        {
            JsonObject jsonobject = JsonUtils.getJsonObject(jsonElement, "status");
            ServerStatusResponse serverstatusresponse = new ServerStatusResponse();

            if (jsonobject.has("description"))
            {
                serverstatusresponse.setServerDescription(jsonDeserializationContext.deserialize(jsonobject.get("description"), IChatComponent.class));
            }

            if (jsonobject.has("players"))
            {
                serverstatusresponse.setPlayerCountData(jsonDeserializationContext.deserialize(jsonobject.get("players"), PlayerCountData.class));
            }

            if (jsonobject.has("version"))
            {
                serverstatusresponse.setProtocolVersionInfo(jsonDeserializationContext.deserialize(jsonobject.get("version"), MinecraftProtocolVersionIdentifier.class));
            }

            if (jsonobject.has("favicon"))
            {
                serverstatusresponse.setFavicon(JsonUtils.getString(jsonobject, "favicon"));
            }

            return serverstatusresponse;
        }

        public JsonElement serialize(ServerStatusResponse serverStatusResponse, Type type, JsonSerializationContext jsonSerializationContext)
        {
            JsonObject jsonobject = new JsonObject();

            if (serverStatusResponse.getServerDescription() != null)
            {
                jsonobject.add("description", jsonSerializationContext.serialize(serverStatusResponse.getServerDescription()));
            }

            if (serverStatusResponse.getPlayerCountData() != null)
            {
                jsonobject.add("players", jsonSerializationContext.serialize(serverStatusResponse.getPlayerCountData()));
            }

            if (serverStatusResponse.getProtocolVersionInfo() != null)
            {
                jsonobject.add("version", jsonSerializationContext.serialize(serverStatusResponse.getProtocolVersionInfo()));
            }

            if (serverStatusResponse.getFavicon() != null)
            {
                jsonobject.addProperty("favicon", serverStatusResponse.getFavicon());
            }

            return jsonobject;
        }
    }
}
