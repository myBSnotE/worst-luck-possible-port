import json, os, re, urllib.request, zipfile, io

OUT = "probe"
os.makedirs(OUT, exist_ok=True)
YARN = "1.21.11+build.6"

def get(url):
    with urllib.request.urlopen(url) as r:
        return r.read()

url = "https://maven.fabricmc.net/net/fabricmc/yarn/%s/yarn-%s-v2.jar" % (urllib.parse.quote(YARN), urllib.parse.quote(YARN))
data = get(url)
z = zipfile.ZipFile(io.BytesIO(data))
name = [n for n in z.namelist() if n.endswith("mappings.tiny")][0]
lines = z.read(name).decode("utf-8").split("\n")

dbg = ["entries: %d" % len(lines), "header: %r" % lines[0]] + ["%r" % l for l in lines[1:8]]
with open(os.path.join(OUT, "debug.txt"), "w") as f:
    f.write("\n".join(dbg))

classes = []
cur = None
for ln in lines:
    if ln.startswith("c\t"):
        p = ln.rstrip("\n").split("\t")
        cur = {"named": p[-1], "first": p[1], "members": []}
        classes.append(cur)
    elif (ln.startswith("\tm\t") or ln.startswith("\tf\t")) and cur is not None:
        p = ln.rstrip("\n").split("\t")
        cur["members"].append("%s %s | %s | %s" % (p[1], p[-1], p[3], p[2]))

FULL = set("""WorldOptions GeneratorOptions SpawnHelper ZombieEntity DrownedEntity PiglinBrain
EndermanEntity SpiderEntity ItemEntity RandomChanceLootCondition TableBonusLootCondition
RandomChanceWithEnchantedBonusLootCondition UniformLootNumberProvider WanderAroundGoal
WanderAroundFarGoal NoPenaltyTargeting AbstractPhase HoldingPhase StrafePlayerPhase
ServerWorldProperties LevelProperties EntityAttributeModifier LocalDifficulty SpawnSettings""".split())
FILTER = {
    "MobEntity": ["quip", "nchant", "rop", "nitialize", "oal", "andom"],
    "ServerWorld": ["tick", "eather", "ain", "hunder", "ightning"],
    "LivingEntity": ["ttribute", "tatusEffect", "quip"],
    "World": ["losestPlayer", "andom"],
    "ProjectileEntity": ["elocity"],
    "PiglinEntity": ["arter"],
    "EnderDragonEntity": ["hase"],
}

out = []
for c in classes:
    simple = c["named"].split("/")[-1].split("$")[-1]
    if simple in FULL:
        out.append("=== %s" % c["named"])
        out.extend("   " + m for m in c["members"])
    elif simple in FILTER:
        out.append("=== %s" % c["named"])
        out.extend("   " + m for m in c["members"] if any(k in m for k in FILTER[simple]))

with open(os.path.join(OUT, "mappings.txt"), "w") as f:
    f.write("yarn %s\nclasses=%d\n" % (YARN, len(classes)))
    f.write("\n".join(out))
print("classes", len(classes), "out", len(out))
