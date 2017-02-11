SHELL := /bin/bash

LATEX=pdflatex
LATEXOPT=--shell-escape
LATEXMK=latexmk
LATEXMKOPT=-pdf
LATEXMKBIBTEX=-bibtex

all: relatorio

cleanaux: 	
	rm -rf *.toc
	rm -rf *.aux
	rm -rf *.log
	rm -rf *.fls
	rm -rf *.fdb_latexmk
	rm -rf *.bbl
	rm -rf *.blg
	rm -rf *.lof
	rm -rf *.out
	rm -fr _minted-rel

clean: cleanaux
	rm -rf *.pdf

relatorio: relatorio.pdf
	xreader relatorio.pdf &> /dev/null &


relatorio.pdf:  main.tex 
	$(LATEXMK) $(LATEXMKBIBTEX) $(LATEXMKOPT) \
	$(LATEXMKBIBTEX) $(CONTINUOUS) \
	-pdflatex="$(LATEX) $(LATEXOPT) %O %S" \
	main.tex
	mv main.pdf relatorio.pdf
	rm -rf *.bbl
	rm -rf *.blg
	rm -rf *.lof
	rm -rf *.out
	rm -rf *.aux
	rm -rf *.toc
	rm -rf *.aux
	rm -rf *.log
	rm -rf *.fls
	rm -rf *.fdb_latexmk


compress:  ./src 
	@echo "Preparar ficheiro Zip para entrega...."
	zip -r TP3.zip ../GREDES-TP3/src/*




