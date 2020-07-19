# TFRRS-Web-Scraper
Web scraper to extract data on American college track and field athletes from www.tfrrs.org and print to text document

This program was designed to extract data for a UBC STAT 306 final project. It compiles information on 800 m outdoor track and field times for American college athletes, and prints summary results to a text document, which was then analyzed in R to build a predictive statistical model for improvement in 800 m times between an athlete's freshman and senior years. 

Detailed description:
  - Uses JSoup to parse web pages and built-in PrintWriter to print result to text file
  - Given a set yearly list page (e.g. https://www.tfrrs.org/archives.html?outdoor=1&year=2010 for the 2010 outdoor season), this web scraper visit the qualifying list for every division; then, for each division, it will visit every athletes that made the divisional qualifying list for the given year
  - For each athlete, the scraper will record their name, school, division, and athlete id
  - It will then parse and record all their career results in the 800 m
  - If the athlete competed in at least one 800 m race in both their freshman and senior years (i.e. four years apart), then the program prints their summary results to the text file: athlete id; name; freshman year; best freshman time; best senior time; school; and 
    division
