import os
import re

BASE_DIR = "."
folders = [f for f in os.listdir(BASE_DIR) if re.match(r"^\d+-", f) and os.path.isdir(f)]
folders.sort()

lines = ["## Table of Contents\n"]
for folder in folders:
    name = re.sub(r"^\d+-", "", folder)
    title = " ".join(word.capitalize() for word in name.replace("_", " ").split())
    lines.append(f"- [{title}]({folder}/index.md)")

toc = "\n".join(lines)

# Inserisce il TOC in index.md
with open("index.md", "r") as f:
    content = f.read()

new_content = re.sub(r"## Table of Contents\n.*?(?=\n##|\Z)", toc + "\n", content, flags=re.DOTALL)

with open("index.md", "w") as f:
    f.write(new_content)
