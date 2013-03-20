In the name of Allah


POSTagger version 1.0
===================
      10 January 2013

This is the README for the *"POSTagger" package* which can helps for predicting
fine-grained and/or coarse-grained POS tags of dependency treebank. This package 
has been developed by [Mojtaba Khallash] (mailto: mkhallash@gmail.com) from _Iran 
University of Science  and Technology (IUST)_.

The home page for the project is:
	http://nlp.iust.ac.ir
	
If you want to use this software for research, please refer to this web address 
in your papers.

The package can be used freely for non-commercial research and educational 
purposes. It comes with no  warranty, but we welcome all comments, bug reports, 
and suggestions for improvements.

Table of contents
------------------

1. Compiling
2. Example of usage
3. Running the package
   <ul>
   a. Convert Dependency To Tagger<br/>
   b. Convert Tag To Dependency<br/>
   c. Train Tagger<br/>
   d. Tagging<br/>
   </ul>
4. References

1. Compiling
----------------

Requirements:
* Version 1.7 or later of the [Java 2 SDK] (http://java.sun.com)
You must add java binary file to system path. <br/>In linux, your
can open `~/.bashrc` file and append this line:
`PATH=$PATH:/<address-of-bin-folder-of-JRE>`

To compile the code, first decompress the package:

in linux:
> tar -xvzf POSTagger.tgz
> cd POSTagger
> sh compile_all.sh

in windows:
> decompress the POSTagger.zip
> compile.bat

You can open the all projects in NetBeans 7.1 (or maybe later) too.

2. Example of usage
---------------------

For any tools in this package a sample Persian treebank exist in "Treebank" 
folder. 
(the full treebank can be download freely from http://dadegan.ir/en).

3. Running the package
-------------------------

This package run in two mode: 

* gui [default mode]<br/>
Simple double click on jar file or run the following command:

> java -jar POSTagger.jar

* command-line<br/>
In order to running package in command-line mode must be set -v flag (visible) 
to 0:

> java -jar POSTagger.jar -v 0

for determining of operational mode, must be set -mode flag to one the following 
values: `D2T|T2D|TR|TG`<br/>
details of each operaional mode describe in the next sections.

3a. Convert Dependency To Tagger
---------------------------------

Goal of this section is converting dependency treebank to tagger format for 
train (with gold tag) or test.

<table>
<tr><td>-i &lt;input conll file&gt;</td><td>input conll file which you want to convert to tagger format</td></tr>
<tr><td>-o &lt;output tag path&gt;</td><td>output file that can be used in tagger</td></tr>
<tr><td>-g &lt;adding gold tag (0|1) [default: 1]&gt;</td><td>this parameter determines converted file used for train (1) or test(0)</td></tr>
<tr><td>-t &lt;type of tag (fpod|cpos) [default: fpos]&gt;</td><td>determine type of tag that want extract from dependency treebank for train</td></tr>
</table>
	

For example:

> java -jar POSTagger.jar -v 0 -mode D2T -i input.conll -o output.lbl -g 1 -t cpos

3b. Convert Tag To Dependency
------------------------------

After predicting tags, can used this section put resulting tags to correct place 
in dependency treebank.

<table>
<tr><td>-i &lt;input tag file&gt;</td><td>input tag file that contains predicted POS tags of each word</td></tr>
<tr><td>-c &lt;input conll file&gt;</td><td>input conll file which you wantr to add predicted POS tags in correct place</td></tr>
<tr><td>-o &lt;output dependency path&gt;</td><td>output dependency file after adding tags</td></tr>
<tr><td>-t &lt;type of tag (fpod|cpos) [default: fpos]&gt;</td><td>type of predicted tag that need for determine place of adding in conll file</td></tr>
</table>

For example:

> java -jar POSTagger.jar -v 0 -mode T2D -i input.lbl -c input.conll -o output.conll -t cpos

3c. Train Tagger
------------------

Using implementation of the part-of-speech tagger described in [1].

<table>
<tr><td>-i &lt;input tag file for train&gt;</td><td>input tag file which contains word_tag of each sentence per line</td></tr>
<tr><td>-m &lt;output model path&gt;</td><td>name of folder for saving training model</td></tr>
<tr><td>-iter &lt;maximim number of iteration [default: 100]&gt;</td><td>maximum number of iteration usef during training</td></tr>
</table>
		
For example:

> java -jar POSTagger.jar -v 0 -mode TR -i input.lbl -m model -iter 100

Requirements:
* "[mxpost.jar] (http://www.inf.ed.ac.uk/resources/nlp/local_doc/MXPOST.html)" for tagging.

3d. Tagging
------------------

These parameters exsit for Tagging:

<table>
<tr><td>-i &lt;input tag file for tagging&gt;</td><td>input tag file contain only words of each sentence per line</td></tr>
<tr><td>-m &lt;trained model path&gt;</td><td>name of folder than saved pre-trained model</td></tr>
<tr><td>-o &lt;output tag path&gt;</td><td>output tag file for adding predicted pos</td></tr>
<tr><td>-g &lt;gold tag file [optional]&gt;</td><td>if you want evaluate predicted POS tag, can set tag file equal to input tag but with gold POS tag</td></tr>
</table>
		
For example:

> java -jar POSTagger.jar -v 0 -mode TG -i input.lbl -m model -o output.lbl -g gold.lbl

Requirements:
* "[mxpost.jar] (http://www.inf.ed.ac.uk/resources/nlp/local_doc/MXPOST.html)" for tagging.

References
------------
[1]	A. Ratnaparkhi, "A maximum entropy model for part-of-speech tagging", in 
Proceedings of  the Empirical Methods in Natural Language Processing 
Conference, University of Pennsylvania, pp. 133-142, 1996.
