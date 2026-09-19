import json, os, re, urllib.request, zipfile, io

OUT = "probe"
os.makedirs(OUT, exist_ok=True)
YARN = "1.21.11+build.6"

def get(url):
    with urllib.request.urlopen(url) as r:
        return r.read()

# fabric-api versions for 1.21.11
meta = get("https://maven.fabricmc.net/net/fabricmc/fabric-api/fabric-api/maven-metadata.xml").decode()
vers = [v for v in re.findall(r"<version>([^<]+)</version>", meta) if v.endswith("+1.21.11")]
with open(os.path.join(OUT, "fabric_api_1_21_11.txt"), "w") as f:
    f.write("\n".join(vers[-10:]))

# yarn mappings
url = "https://maven.fabricmc.net/net/fabricmc/yarn/%s/yarn-%s-v2.jar" % (urllib.parse.quote(YARN), urllib.parse.quote(YARN))
data = get(url)
z = zipfile.ZipFile(io.BytesIO(data))
name = [n for n in z.namelist() if n.endswith("mappings.tiny")][0]
lines = z.read(name).decode("utf-8").split("\n")

# parse tiny v2
classes = {}  # named -> {"inter":..., "members":[str]}
cur = None
header = lines[0]
for ln in lines:
    if ln.startswith("c\t"):
        p = ln.split("\t")
        cur = {"official": p[1], "inter": p[2] if len(p) > 2 else "", "named": p[3] if len(p) > 3 else "", "members": []}
        classes[cur["named"]] = cur
    elif ln.startswith("\tm\t") or ln.startswith("\tf\t"):
        if cur is not None:
            p = ln.split("\t")
            kind = p[1]
            desc = p[2]
            inter = p[4] if len(p) > 4 else ""
            named = p[5] if len(p) > 5 else ""
            cur["members"].append("%s %s %s %s" % (kind, named, inter, desc))

WANT_FULL = [
    "world/gen/WorldGenerationProgressListener",
]

# classes to dump fully (small/medium)
FULL = [
    "WorldOptions", "GeneratorOptions", "SpawnHelper", "ZombieEntity", "DrownedEntity",
    "PiglinBrain", "EndermanEntity", "SpiderEntity", "ItemEntity", "RandomChanceLootCondition",
    "RandomChanceWithEnchantedBonusLootCondition", "TableBonusLootCondition", "UniformLootNumberProvider",
    "WanderAroundGoal", "WanderAroundFarGoal", "NoPenaltyTargeting", "AbstractPhase",
    "HoldingPhase", "StrafePlayerPhase", "ServerWorldProperties", "LevelProperties",
    "EntityAttributeModifier", "SpawnSettings", "SpawnGroup", "LocalDifficulty",
]
FILTER = {
    "MobEntity": ["equip", "enchant", "drop", "initialize", "Goal", "goal", "random", "Random"],
    "ServerWorld": ["tick", "Weather", "weather", "rain", "thunder", "Lightning", "random"],
    "LivingEntity": ["Attribute", "attribute", "StatusEffect", "Equipment", "equip"],
    "World": ["ClosestPlayer", "closest", "random", "Random"],
    "ProjectileEntity": ["setVelocity", "velocity"],
    "Biome": ["spawn", "Spawn"],
    "EnderDragonEntity": ["phase", "Phase"],
    "PiglinEntity": ["barter", "Barter"],
}

out = []
for named, c in classes.items():
    simple = named.split("/")[-1].split("$")[-1]
    if simple in FULL:
        out.append("=== %s   (%s)" % (named, c["inter"]))
        out.extend("   " + m for m in c["members"])
    elif simple in FILTER:
        kws = FILTER[simple]
        out.append("=== %s   (%s)" % (named, c["inter"]))
        for m in c["members"]:
            if any(k in m for k in kws):
                out.append("   " + m)

with open(os.path.join(OUT, "mappings.txt"), "w") as f:
    f.write("yarn %s\n" % YARN)
    f.write("\n".join(out))
print("classes:", len(classes), "outlines:", len(out))
print("fabric-api:", vers[-5:])
