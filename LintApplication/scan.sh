
echo "hello,world" >> b.txt

grep -rn -E --color=auto 'getInstalledPackages|getInstalledApplications' ./ --include='*.smali' > ../TripartiteLibrary.txt

grep -rn --color=auto getImei ./ --include='*.smali' > ../TripartiteLibrary.txt

grep -rn --color=auto getMac ./ --include='*.smali' > ../TripartiteLibrary.txt

grep -rn -E --color=auto "getLastKnownLocation|LocationListener" ./ --include='*.smali' > ../TripartiteLibrary.txt

grep -rn --color=auto 'Telephony$Sms;->CONTENT_URI' ./ --include='*.smali' > ../TripartiteLibrary.txt

grep -rn --color=auto 'CallLog$Calls;->CONTENT_URI' ./ --include='*.smali' > ../TripartiteLibrary.txt

grep -rn --color=auto 'ContactsContract$Contacts;->CONTENT_URI' ./ --include='*.smali' > ../TripartiteLibrary.txt
