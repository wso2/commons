1. Enable VFS transport and create a stockQuoteProxy (Synapse stockQuoteProxy.xml)

2. Create a folder stucture like below
C:\PERSONAL\VFS
├───Failures
├───Original
├───out
└───Pass

3. Deploy SimpleStockQuote Service using AXIS2

4. Place the Test.XML & aaa.xml to Original folder

5. Results
Test.XML should move to Pass folder
aaa.xml should move to Failures folder
AXIS2 response should saved to out folder (Using Test.xml)


VFS - Sample 254 for basic functionality