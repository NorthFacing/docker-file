#!/bin/sh
echo "...............begin RAS  &  CMS compiling! "
JY_RAS_CMS_HOME=/root/joyoung/workspace
cd $JY_RAS_CMS_HOME

echo "jy_ras compileing..............."
cd jy_ras
sh precompile.sh

echo "jy_cms compileing................ "
cd ../jy_cms
sh precompile.sh
