# Scala-Path

This project aims to create a path finding engine for different scenarios in Scala

## Table of Contents

<ul>
  {% assign sections = site.pages
       | where:"name","index.html"
       | sort:"url" %}
  {% for page in sections %}
    {% if page.url != "/" %}
      <li>
        <a href="{{ page.url | relative_url }}">
          {{ page.title }}
        </a>
      </li>
    {% endif %}
  {% endfor %}
</ul>
