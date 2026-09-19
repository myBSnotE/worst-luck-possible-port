import json, os, re, sys, urllib.request, zipfile, io

OUT = "probe"
os.makedirs(OUT, exist_ok=True)

def get(url):
    with urllib.request.urlopen(url) as r:
        return r.read()

# 1) Fabric meta: game versions, yarn, loader
game = json.loads(get("https://meta.fabricmc.net/v2/versions/game"))
stable = [g["version"] for g in game if g["stable"]][:40]
loader = json.loads(get("https://meta.fabricmc.net/v2/versions/loader"))
loader_stable = [l["version"] for l in loader if l["stable"]][:5]

summary = {"stable_game_versions": stable, "loader": loader_stable}

# yarn versions for the newest few stable game versions
yarn_by_version = {}
for v in stable[:8]:
    try:
        y = json.loads(get("https://meta.fabricmc.net/v2/versions/yarn/" + urllib.parse.quote(v)))
        yarn_by_version[v] = [e["version"] for e in y[:3]]
    except Exception as e:
        yarn_by_version[v] = ["ERR " + str(e)]
summary["yarn"] = yarn_by_version

# fabric-api versions
try:
    meta = get("https://maven.fabricmc.net/net/fabricmc/fabric-api/fabric-api/maven-metadata.xml").decode()
    versions = re.findall(r"<version>([^<]+)</version>", meta)
    summary["fabric_api_latest"] = versions[-25:]
except Exception as e:
    summary["fabric_api_latest"] = ["ERR " + str(e)]

# loom versions
try:
    meta = get("https://maven.fabricmc.net/fabric-loom/fabric-loom.gradle.plugin/maven-metadata.xml").decode()
    summary["loom"] = re.findall(r"<version>([^<]+)</version>", meta)[-10:]
except Exception as e:
    summary["loom"] = ["ERR " + str(e)]

with open(os.path.join(OUT, "versions.json"), "w") as f:
    json.dump(summary, f, indent=2)

print(json.dumps(summary, indent=2)[:4000])
