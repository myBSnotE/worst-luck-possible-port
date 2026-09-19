import json, os, re, urllib.request, urllib.parse, zipfile, io

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

FULL = set("""SpawnHelper SpawnSettings SpawnEntry SpawnDensity PhaseManager PhaseType AbstractPhase
HoldingPhase LandingApproachPhase LandingPhase SittingPhase StrafePlayerPhase ChargingPlayerPhase
SpawnRestriction SpawnGroup ServerChunkManager""".split())
FILTER = {
    "MobEntity": ["espawn", "ersistent", "panwable", "pawn"],
    "EnderDragonEntity": ["hase", "erch", "arget"],
    "Entity": ["emove", "iscard"],
    "EntityType": ["reate", "pawn", "GHAST", "STRIDER", "ZOMBIFIED"],
    "WorldChunk": ["pawn"],
    "Biome": ["pawn"],
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

with open(os.path.join(OUT, "mappings2.txt"), "w") as f:
    f.write("yarn %s\nclasses=%d\n" % (YARN, len(classes)))
    f.write("\n".join(out))
print("classes", len(classes), "out", len(out))
