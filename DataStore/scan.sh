
if [ $# -lt 2 ]; then
    echo '缺少必要参数'
    exit 1
fi

# 参数为:
# 1. git仓库地址 例如： git@git.xxx.com:terminal/android/android-yyy/yyy.git
# 2. 分支名
# 3. 新增自定义扫描关键字（两个或者两个以上的条件用|拼接字符串, 多个关键字用,分割） 例如："getInstalledPackages|getInstalledApplications,getImei"
# 4. 是否需要扫描默认的数据 0 需要  1不需要

gitPath=$1
branchName=$2
customStr=$3
default=$4

# shellcheck disable=SC2077
if [ "$default"="" ]; then
    default=0
fi

echo "********************** 从 git clone 项目 ****************************"
git clone -b "$branchName" "$gitPath"

projectGitName=${gitPath##*/}
projectName=$(echo "$projectGitName" | cut -d . -f1)
# shellcheck disable=SC2164
cd "${projectName}"

echo "********************** 本地打包 ****************************"
# ./gradlew assembleRelease
./gradlew clean assembleInnerArm64V8aRelease

# shellcheck disable=SC2103
cd ..

echo "********************** 反编译 apk 文件 ****************************"
java -jar apktool_2.4.1.jar d -f "${projectName}"/app/build/outputs/apk/innerArm64V8a/release/app-inner-arm64V8a-release.apk -only-main-classes

echo "********************** 检索字符串并写入文件 ****************************"
if [ $default -eq 0 ]; then

    # shellcheck disable=SC2006
    string=`grep -rn -E --color=auto 'getInstalledPackages|getInstalledApplications' ./ --include='*.smali' grep java `
    echo "$string" >> installed.txt

    # shellcheck disable=SC2006
    string=`grep -rn --color=auto getImei ./ --include='*.smali' grep java `
    echo "$string" >> imei.txt

    # shellcheck disable=SC2006
    string=`grep -rn --color=auto getMac ./ --include='*.smali' grep java `
    echo "$string" >> mac.txt

    # shellcheck disable=SC2006
    string=`grep -rn -E --color=auto "getLastKnownLocation|LocationListener" ./ --include='*.smali' grep java `
    echo "$string" >> location.txt

    # shellcheck disable=SC2016
    # shellcheck disable=SC2006
    string=`grep -rn --color=auto 'Telephony$Sms;->CONTENT_URI' ./ --include='*.smali' grep java `
    echo "$string" >> sms.txt

    # shellcheck disable=SC2016
    # shellcheck disable=SC2006
    string=`grep -rn --color=auto 'CallLog$Calls;->CONTENT_URI' ./ --include='*.smali' grep java `
    echo "$string" >> calls.txt

    # shellcheck disable=SC2016
    # shellcheck disable=SC2006
    string=`grep -rn --color=auto 'ContactsContract$Contacts;->CONTENT_URI' ./ --include='*.smali' grep java `
    echo "$string" >> contract.txt
fi

# shellcheck disable=SC2077
if [ "$customStr"="" ]; then
    OLD_IFS="$IFS"
    IFS=","
    # shellcheck disable=SC2206
    arr=($customStr)
    IFS="$OLD_IFS"
    # shellcheck disable=SC2068
    for s in ${arr[@]}
    do
    result=$(echo "$s" | grep "|")
    if [[ "$result" != "" ]]
    then
        # shellcheck disable=SC2006
        string=`grep -rn -E --color=auto "$s" ./ --include='*.smali' grep java `
        echo "$string" >> "${s}".txt
    else
        # shellcheck disable=SC2006
        string=`grep -rn --color=auto "$s" ./ --include='*.smali' grep java `
        echo "$string" >> "${s}".txt
    fi

    done
fi

javac StringToHtml.java
java StringToHtml "$projectName" $default "$customStr"
open scan.html