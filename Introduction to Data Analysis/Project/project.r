
### TESTING
#library(foreign)
#mydata = read.spss("C:/Users/Kevin/Desktop/Project/ATP W63.sav", to.data.frame = TRUE)
#attr(mydata, "variable.labels")[["VAR"]]
#write.csv(mydata, "C:/Users/Kevin/Desktop/Project/test.csv", row.names = FALSE)
#getwd()

#trends = read.csv("C:/Users/Kevin/Desktop/Project/test.csv")
#trends

library(foreign)
library(reshape)
library(RColorBrewer)
library(ggplot2)
#mydata = read.spss("C:/Users/Kevin/Desktop/Project/ATP W63.sav", header = TRUE, sep = ",")
#write.csv(mydata, "C:/Users/Kevin/Desktop/Project/trends.csv")

trends = read.csv("C:/Users/Kevin/Desktop/Project/trends.csv")
trends

df = data.frame(trends)
nrow(df)
ncol(df)
summary(df)

myPalette <- brewer.pal(10, "Set2")

# Parent and Kids Time Together
tab1 = table(trends$PARENTTIME_W63)
tab1
pct1 = round(tab1/sum(tab1)*100)
pct1
labs1 = paste(sort(unique(trends$PARENTTIME_W63)), pct1)
labs1 = paste(labs1,"%",sep="")
pie1 = pie(tab1, main="Parent and Kids Time Together", labels=labs1, col=myPalette, border="black")
bar1x = barplot(tab1, main="Parent and Kids Time Together", col=myPalette, border="black", ylim=c(0,2500))
bar1y = as.matrix(tab1)
text1 = text(bar1x,bar1y+50,labels=as.character(bar1y))



# Rating One's Own Parenting
tab2 = table(trends$PARENTJOB_W63)
tab2
pct2 = round(tab2/sum(tab2)*100)
pct2
labs2 = paste(sort(unique(trends$PARENTJOB_W63)), pct2)
labs2 = paste(labs2,"%",sep="")
pie2 = pie(tab2, main="Rating One's Own Parenting", labels=labs2, col=myPalette, border="black")
bar2x = barplot(tab2, main="Rating One's Own Parenting", col=myPalette, border="black", ylim=c(0,2500))
bar2y = as.matrix(tab2)
text2 = text(bar2x,bar2y+50,labels=as.character(bar2y))



# Perceived Risks/Benefits of Smartphones for Children aged 11 or younger
tab3 = table(trends$RISKBEN_W63)
tab3
pct3 = round(tab3/sum(tab3)*100)
pct3
labs3 = paste(sort(unique(trends$RISKBEN_W63)), pct3)
labs3 = paste(labs3,"%",sep="")
pie3 = pie(tab3, main="Perceived Risks/Benefits of Smartphones for Children aged 11 or younger", labels=labs3, col=myPalette, border="black")
bar3x = barplot(tab3, main="Perceived Risks/Benefits of Smartphones for Children aged 11 or younger", col=myPalette, border="black", ylim=c(0,3000))
bar3y = as.matrix(tab3)
text3 = text(bar3x,bar3y+50,labels=as.character(bar3y))


# Perceived Risks/Benefits of Smartphones for Children aged 11 or younger to learn Social Skills
tab4 = table(trends$MOBILEHELP_a_W63)
tab4
pct4 = round(tab4/sum(tab4)*100)
pct4
labs4 = paste(sort(unique(trends$MOBILEHELP_a_W63)), pct4)
labs4 = paste(labs4,"%",sep="")
pie4 = pie(tab4, main="Perceived Risks/Benefits of Smartphones for Children aged 11 or younger to learn Social Skills", labels=labs4, col=myPalette, border="black")
bar4x = barplot(tab4, main="Perceived Risks/Benefits of Smartphones for Children aged 11 or younger to learn Social Skills", col=myPalette, border="black", ylim=c(0,2000))
bar4y = as.matrix(tab4)
text4 = text(bar4x,bar4y+50,labels=as.character(bar4y))


# Perceived Risks/Benefits of Smartphones for Children aged 11 or younger to be Creative
tab5 = table(trends$MOBILEHELP_b_W63)
tab5
pct5 = round(tab5/sum(tab5)*100)
pct5
labs5 = paste(sort(unique(trends$MOBILEHELP_b_W63)), pct5)
labs5 = paste(labs5,"%",sep="")
pie5 = pie(tab5, main="Perceived Risks/Benefits of Smartphones for Children aged 11 or younger to be Creative", labels=labs5, col=myPalette, border="black")
bar5x = barplot(tab5, main="Perceived Risks/Benefits of Smartphones for Children aged 11 or younger to be Creative", col=myPalette, border="black", ylim=c(0,1250))
bar5y = as.matrix(tab5)
text5 = text(bar5x,bar5y+50,labels=as.character(bar5y))


# Perceived Risks/Benefits of Smartphones for Children aged 11 or younger to pursue their Hobbies and Interests
tab6 = table(trends$MOBILEHELP_c_W63)
tab6
pct6 = round(tab6/sum(tab6)*100)
pct6
labs6 = paste(sort(unique(trends$MOBILEHELP_c_W63)), pct6)
labs6 = paste(labs6,"%",sep="")
pie6 = pie(tab6, main="Perceived Risks/Benefits of Smartphones for Children aged 11 or younger to pursue their Hobbies and Interests", labels=labs6, col=myPalette, border="black")
bar6x = barplot(tab6, main="Perceived Risks/Benefits of Smartphones for Children aged 11 or younger to pursue their Hobbies and Interests", col=myPalette, border="black", ylim=c(0,1250))
bar6y = as.matrix(tab6)
text6 = text(bar6x,bar6y+50,labels=as.character(bar6y))


# Perceived Risks/Benefits of Smartphones for Children aged 11 or younger to do well in School
tab7 = table(trends$MOBILEHELP_d_W63)
tab7
pct7 = round(tab7/sum(tab7)*100)
pct7
labs7 = paste(sort(unique(trends$MOBILEHELP_d_W63)), pct7)
labs7 = paste(labs7,"%",sep="")
pie7 = pie(tab7, main="Perceived Risks/Benefits of Smartphones for Children aged 11 or younger to do well in School", labels=labs7, col=myPalette, border="black")
bar7x = barplot(tab7, main="Perceived Risks/Benefits of Smartphones for Children aged 11 or younger to do well in School", col=myPalette, border="black", ylim=c(0,1250))
bar7y = as.matrix(tab7)
text7 = text(bar7x,bar7y+50,labels=as.character(bar7y))


# Perceived Risks/Benefits of Smartphones for Children aged 11 or younger to develop Healthy Relationships
tab8 = table(trends$MOBILEHELP_e_W63)
tab8
pct8 = round(tab8/sum(tab8)*100)
pct8
labs8 = paste(sort(unique(trends$MOBILEHELP_e_W63)), pct8)
labs8 = paste(labs8,"%",sep="")
pie8 = pie(tab8, main="Perceived Risks/Benefits of Smartphones for Children aged 11 or younger to develop Healthy Relationships", labels=labs8, col=myPalette, border="black")
bar8x = barplot(tab8, main="Perceived Risks/Benefits of Smartphones for Children aged 11 or younger to develop Healthy Relationships", col=myPalette, border="black", ylim=c(0,1500))
bar8y = as.matrix(tab8)
text8 = text(bar8x,bar8y+50,labels=as.character(bar8y))


# Age where it is Acceptable for Children to use Social Media Sites
tab9 = table(trends$ACCEPTUSE_a_W63)
tab9
pct9 = round(tab9/sum(tab9)*100)
pct9
labs9 = paste(sort(unique(trends$ACCEPTUSE_a_W63)), pct9)
labs9 = paste(labs9,"%",sep="")
pie9 = pie(tab9, main="Age where it is Acceptable for Children to use Social Media Sites", labels=labs9, col=myPalette, border="black")
bar9x = barplot(tab9, main="Age where it is Acceptable for Children to use Social Media Sites", col=myPalette, border="black", ylim=c(0,2000))
bar9y = as.matrix(tab9)
text9 = text(bar9x,bar9y+50,labels=as.character(bar9y))


# Age where it is Acceptable for Children to play Video Games
tab10 = table(trends$ACCEPTUSE_b_W63)
tab10
pct10 = round(tab10/sum(tab10)*100)
pct10
labs10 = paste(sort(unique(trends$ACCEPTUSE_b_W63)), pct10)
labs10 = paste(labs10,"%",sep="")
pie10 = pie(tab10, main="Age where it is Acceptable for Children to play Video Games", labels=labs10, col=myPalette, border="black")
bar10x = barplot(tab10, main="Age where it is Acceptable for Children to play Video Games", col=myPalette, border="black", ylim=c(0,1750))
bar10y = as.matrix(tab10)
text10 = text(bar10x,bar10y+50,labels=as.character(bar10y))


# Age where it is Acceptable for Children to have their own Smartphone
tab11 = table(trends$ACCEPTOWN_a_W63)
tab11
pct11 = round(tab11/sum(tab11)*100)
pct11
labs11 = paste(sort(unique(trends$ACCEPTOWN_a_W63)), pct11)
labs11 = paste(labs11,"%",sep="")
pie11 = pie(tab11, main="Age where it is Acceptable for Children to have their own Smartphone", labels=labs11, col=myPalette, border="black")
bar11x = barplot(tab11, main="Age where it is Acceptable for Children to have their own Smartphone", col=myPalette, border="black", ylim=c(0,2000))
bar11y = as.matrix(tab11)
text11 = text(bar11x,bar11y+50,labels=as.character(bar11y))

# Age where it is Acceptable for Children to have their own Tablet Computer
tab12 = table(trends$ACCEPTOWN_b_W63)
tab12
pct12 = round(tab12/sum(tab12)*100)
pct12
labs12 = paste(sort(unique(trends$ACCEPTOWN_b_W63)), pct12)
labs12 = paste(labs12,"%",sep="")
pie12 = pie(tab12, main="Age where it is Acceptable for Children to have their own Tablet Computer", labels=labs12, col=myPalette, border="black")
bar12x = barplot(tab12, main="Age where it is Acceptable for Children to have their own Tablet Computer", col=myPalette, border="black", ylim=c(0,1250))
bar12y = as.matrix(tab12)
text12 = text(bar12x,bar12y+50,labels=as.character(bar12y))


# Confidence in one's own ability to determine appropriate Screen Time for their Children aged 11 or younger
tab13 = table(trends$APPRO_W63)
tab13
pct13 = round(tab13/sum(tab13)*100)
pct13
labs13 = paste(sort(unique(trends$APPRO_W63)), pct13)
labs13 = paste(labs13,"%",sep="")
pie13 = pie(tab13, main="Confidence in one's own ability to determine appropriate Screen Time for their Children aged 11 or younger", labels=labs13, col=myPalette, border="black")
bar13x = barplot(tab13, main="Confidence in one's own ability to determine appropriate Screen Time for their Children aged 11 or younger", col=myPalette, border="black", ylim=c(0,1500))
bar13y = as.matrix(tab13)
text13 = text(bar13x,bar13y+50,labels=as.character(bar13y))


# How often one is distracted by their Smartphones while spending time with their Children
tab14 = table(trends$DISTRACTED_W63)
tab14
pct14 = round(tab14/sum(tab14)*100)
pct14
labs14 = paste(sort(unique(trends$DISTRACTED_W63)), pct14)
labs14 = paste(labs14,"%",sep="")
pie14 = pie(tab14, main="How often one is distracted by their Smarthphones while spending time with their Children", labels=labs14, col=myPalette, border="black")
bar14x = barplot(tab14, main="How often one is distracted by their Smarthphones while spending time with their Children", col=myPalette, border="black", ylim=c(0,2000))
bar14y = as.matrix(tab14)
text14 = text(bar14x,bar14y+50,labels=as.character(bar14y))


# Parenting today is ...
tab15 = table(trends$EASYHARD_W63)
tab15
pct15 = round(tab15/sum(tab15)*100)
pct15
labs15 = paste(sort(unique(trends$EASYHARD_W63)), pct15)
labs15 = paste(labs15,"%",sep="")
pie15 = pie(tab15, main="Parenting today is ...", labels=labs15, col=myPalette, border="black")
bar15x = barplot(tab15, main="Parenting today is ...", col=myPalette, border="black", ylim=c(0,2750))
bar15y = as.matrix(tab15)
text15 = text(bar15x,bar15y+50,labels=as.character(bar15y))


# Parent's Income
tab16 = table(trends$F_INCOME)
tab16 = tab16[c('Less than $10,000', '$10,000 to less than $20,000', '$20,000 to less than $30,000', 
                '$30,000 to less than $40,000', '$40,000 to less than $50,000', '$50,000 to less than $75,000', 
                '$75,000 to less than $100,000', '$100,000 to less than $150,000', '$150,000 or more',
                'Refused')]
tab16
pct16 = round(tab16/sum(tab16)*100)
pct16
labs16 = paste(sort(unique(trends$F_INCOME)), pct16)
labs16 = paste(labs16,"%",sep="")
pie16 = pie(tab16, main="Parent's Income", labels=labs16, col=myPalette, border="black")
bar16x = barplot(tab16, main="Parent's Income", col=myPalette, border="black", ylim=c(0,2750))
bar16y = as.matrix(tab16)
text16 = text(bar16x,bar16y+50,labels=as.character(bar16y))


# Parent's Income (Recoded) ** Decide Not to Use **
#tab17 = table(trends$F_INCOME_RECODE)
#tab17 = tab17[c('<$30,000', '$30-$74,999', '$75,000+', 'Refused')]
#tab17
#pct17 = round(tab17/sum(tab17)*100)
#pct17
#labs17 = paste(sort(unique(trends$F_INCOME_RECODE)), pct17)
#labs17 = paste(labs17,"%",sep="")
#pie17 = pie(tab17, main="Parent's Income (Recoded)", labels=labs17, col=myPalette, border="black")
#bar17x = barplot(tab17, main="Parent's Income (Recoded)", col=myPalette, border="black", ylim=c(0,2750))
#bar17y = as.matrix(tab17)
#text17 = text(bar17x,bar17y+50,labels=as.character(bar17y))


### Working with Tab 16
### Mapping Income Data
incomes = trends$F_INCOME
incomes = incomes[! incomes %in% c('Refused')]   # Removing 'Refused' because it gives no data
incomes = replace(incomes, incomes == 'Less than $10,000', 1)
incomes = replace(incomes, incomes == '$10,000 to less than $20,000', 2)
incomes = replace(incomes, incomes == '$20,000 to less than $30,000', 3)
incomes = replace(incomes, incomes == '$30,000 to less than $40,000', 4)
incomes = replace(incomes, incomes == '$40,000 to less than $50,000', 5)
incomes = replace(incomes, incomes == '$50,000 to less than $75,000', 6)
incomes = replace(incomes, incomes == '$75,000 to less than $100,000', 7)
incomes = replace(incomes, incomes == '$100,000 to less than $150,000', 8)
incomes = replace(incomes, incomes == '$150,000 or more', 9)
incomes = strtoi(incomes)

### Below is a Chi-Squared Test, Experimentation ###

### Chi-Squared Calculations for Poisson Distribution Goodness of Fit
counts <- table(incomes-1)
mu <- mean(incomes-1)
expected <- dpois(as.numeric(names(counts)), mu) * length(incomes)
x <- (counts - expected)^2 / expected

### From the Chi-Squared Results below, Poisson Distribution is not a good fit
### values are NOT close to 1
print(round(x, 2)) # Terms in the chi-squared statistic

### Expected to Actual Results
print(rbind(Expected = round(expected, 0), Actual=counts))

### Plotting the Poisson Distribution, but it is not a good fit
X <- data.frame(Index=1:length(incomes), Count=incomes)
g <- ggplot(X, aes(Index, Count)) + geom_smooth(size=2) + geom_point(size=2, alpha=1/2)
print(g)


### Using the Mapping Incomes Data to Do Logistic Regression since I have categorical responses ###

# Parent's Time on Smartphone, Social Media, and Video Games
#parents_time_smartphone = trends$TIMESPEND_a_W63
#parents_time_social_media = trends$TIMESPEND_b_W63
#parents_time_social_video_games = trends$TIMESPEND_c_W63

#tab18 = table(parents_time_smartphone)
#tab19 = table(parents_time_social_media)
#tab20 = table(parents_time_social_video_games)
#tab18
#tab19
#tab20

# Mapping Parent's Time on Smartphone, Social Media, and Video Games
#parents_time_smartphone = parents_time_smartphone[! parents_time_smartphone %in% c('Refused')]   # Removing 'Refused' because it gives no data
#parents_time_smartphone <- parents_time_smartphone[!is.na(parents_time_smartphone)]   # Removing 'NA' because it gives no data
#parents_time_smartphone = replace(parents_time_smartphone, parents_time_smartphone=='Too little time', 1)
#parents_time_smartphone = replace(parents_time_smartphone, parents_time_smartphone=='About the right amount of time', 2)
#parents_time_smartphone = replace(parents_time_smartphone, parents_time_smartphone=='Too much time', 3)
#parents_time_smartphone = strtoi(parents_time_smartphone)

#parents_time_social_media = parents_time_social_media[! parents_time_social_media %in% c('Refused')]
#parents_time_social_media <- parents_time_social_media[!is.na(parents_time_social_media)]   # Removing 'NA' because it gives no data
#parents_time_social_media = replace(parents_time_social_media, parents_time_social_media=='Too little time', 1)
#parents_time_social_media = replace(parents_time_social_media, parents_time_social_media=='About the right amount of time', 2)
#parents_time_social_media = replace(parents_time_social_media, parents_time_social_media=='Too much time', 3)
#parents_time_social_media = strtoi(parents_time_social_media)

#parents_time_social_video_games = parents_time_social_video_games[! parents_time_social_video_games %in% c('Refused')]
#parents_time_social_video_games <- parents_time_social_video_games[!is.na(parents_time_social_video_games)]   # Removing 'NA' because it gives no data
#parents_time_social_video_games = replace(parents_time_social_video_games, parents_time_social_video_games=='Too little time', 1)
#parents_time_social_video_games = replace(parents_time_social_video_games, parents_time_social_video_games=='About the right amount of time', 2)
#parents_time_social_video_games = replace(parents_time_social_video_games, parents_time_social_video_games=='Too much time', 3)
#parents_time_social_video_games = strtoi(parents_time_social_video_games)


# Child's Time on Smartphone, Social Media, and Video Games
#childs_time_smartphone = trends$CHILDTIME_a_W63
#childs_time_social_media = trends$CHILDTIME_b_W63
#childs_time_video_games = trends$CHILDTIME_c_W63

#tab21 = table(childs_time_smartphone)
#tab22 = table(childs_time_social_media)
#tab23 = table(childs_time_video_games)
#tab21
#tab22
#tab23

# Mapping Child's Time on Smartphone, Social Media, and Video Games
#childs_time_smartphone = childs_time_smartphone[! childs_time_smartphone %in% c('Refused')]   # Removing 'Refused' because it gives no data
#childs_time_smartphone <- childs_time_smartphone[!is.na(childs_time_smartphone)]   # Removing 'NA' because it gives no data
#childs_time_smartphone = replace(childs_time_smartphone, childs_time_smartphone=='Too little time', 1)
#childs_time_smartphone = replace(childs_time_smartphone, childs_time_smartphone=='About the right amount of time', 2)
#childs_time_smartphone = replace(childs_time_smartphone, childs_time_smartphone=='Too much time', 3)
#childs_time_smartphone = strtoi(childs_time_smartphone)

#childs_time_social_media = childs_time_social_media[! childs_time_social_media %in% c('Refused')]
#childs_time_social_media <- childs_time_social_media[!is.na(childs_time_social_media)]   # Removing 'NA' because it gives no data
#childs_time_social_media = replace(childs_time_social_media, childs_time_social_media=='Too little time', 1)
#childs_time_social_media = replace(childs_time_social_media, childs_time_social_media=='About the right amount of time', 2)
#childs_time_social_media = replace(childs_time_social_media, childs_time_social_media=='Too much time', 3)
#childs_time_social_media = strtoi(childs_time_social_media)

#childs_time_video_games = childs_time_video_games[! childs_time_video_games %in% c('Refused')]
#childs_time_video_games <- childs_time_video_games[!is.na(childs_time_video_games)]   # Removing 'NA' because it gives no data
#childs_time_video_games = replace(childs_time_video_games, childs_time_video_games=='Too little time', 1)
#childs_time_video_games = replace(childs_time_video_games, childs_time_video_games=='About the right amount of time', 2)
#childs_time_video_games = replace(childs_time_video_games, childs_time_video_games=='Too much time', 3)
#childs_time_video_games = strtoi(childs_time_video_games)


df = data.frame(trends$F_INCOME, trends$TIMESPEND_a_W63, trends$TIMESPEND_b_W63, trends$TIMESPEND_c_W63, trends$CHILDTIME_a_W63, trends$CHILDTIME_b_W63, trends$CHILDTIME_b_W63)
colnames(df) = c("Income", "Parents_Time_SP", "Parents_Time_SM", "Parents_Time_VG", "Childs_Time_SP", "Childs_Time_SM", "Childs_Time_VG")
print(head(df))

df[df == 'Refused'] = NA
df[df == 'Less than $10,000'] = 1
df[df == '$10,000 to less than $20,000'] = 2
df[df == '$20,000 to less than $30,000'] = 3
df[df == '$30,000 to less than $40,000'] = 4
df[df == '$40,000 to less than $50,000'] = 5
df[df == '$50,000 to less than $75,000'] = 6
df[df == '$75,000 to less than $100,000'] = 7
df[df == '$100,000 to less than $150,000']= 8
df[df == '$150,000 or more'] = 9
print(head(df))

df[df == 'Too little time'] = 1
df[df == 'About the right amount of time'] = 2
df[df == 'Too much time'] = 3
print(head(df))

df_mapped_income_time = df

# Update all values to numeric data instead of string/character data
df_mapped_income_time$Income <- as.numeric(as.character(df_mapped_income_time$Income))
df_mapped_income_time$Parents_Time_SP <- as.numeric(as.character(df_mapped_income_time$Parents_Time_SP))
df_mapped_income_time$Parents_Time_SM <- as.numeric(as.character(df_mapped_income_time$Parents_Time_SM))
df_mapped_income_time$Parents_Time_VG <- as.numeric(as.character(df_mapped_income_time$Parents_Time_VG))
df_mapped_income_time$Childs_Time_SP <- as.numeric(as.character(df_mapped_income_time$Childs_Time_SP))
df_mapped_income_time$Childs_Time_SM <- as.numeric(as.character(df_mapped_income_time$Childs_Time_SM))
df_mapped_income_time$Childs_Time_VG <- as.numeric(as.character(df_mapped_income_time$Childs_Time_VG))


# Finding all unique values in a dataframe
#as.character(unique(unlist(df_mapped_income_time)))

# After running the following, all 7 columns in the dataframe had some missing data, will need to do logistic regression individually on one variable at a time
#complete_income_time_df = df_mapped_income_time[complete.cases(df_mapped_income_time),]
#nrows(complete_income_time_df)


# Creating smaller dataframes from the initial dataframe - df_mapped_income_time
df_income_parents_sp = df_mapped_income_time[, c('Income', 'Parents_Time_SP')]   # Parent's income Vs. Parent's Time on Smartphone
df_income_parents_sm = df_mapped_income_time[, c('Income', 'Parents_Time_SM')]   # Parent's income Vs. Parent's Time on Social Media
df_income_parents_vg = df_mapped_income_time[, c('Income', 'Parents_Time_VG')]   # Parent's income Vs. Parent's Time on Video Game

df_income_childs_sp = df_mapped_income_time[, c('Income', 'Childs_Time_SP')]   # Parent's income Vs. Child's Time on Smartphone
df_income_childs_sm = df_mapped_income_time[, c('Income', 'Childs_Time_SM')]   # Parent's income Vs. Child's Time on Social Media
df_income_childs_vg = df_mapped_income_time[, c('Income', 'Childs_Time_VG')]   # Parent's income Vs. Child's Time on Video Games

df_parents_sp_childs_sp = df_mapped_income_time[, c('Parents_Time_SP', 'Childs_Time_SP')]   # Parent's Time on Smartphone Vs. Child's Time on Smartphone
df_parents_sm_childs_sm = df_mapped_income_time[, c('Parents_Time_SM', 'Childs_Time_SM')]   # Parent's Time on Social Media Vs. Child's Time on Social Media
df_parents_vg_childs_vg = df_mapped_income_time[, c('Parents_Time_VG', 'Childs_Time_VG')]   # Parent's Time on Video Games Vs. Child's Time on Video Games


# Logistic Regression Models drawn from dataframes, 9 models
model_income_parents_sp = glm(formula = Parents_Time_SP ~ Income, family = gaussian, data = df_income_parents_sp, na.action = na.omit)
model_income_parents_sm = glm(formula = Parents_Time_SM ~ Income, family = gaussian, data = df_income_parents_sm, na.action = na.omit)
model_income_parents_vg = glm(formula = Parents_Time_VG ~ Income, family = gaussian, data = df_income_parents_vg, na.action = na.omit)

model_income_childs_sp = glm(formula = Childs_Time_SP ~ Income, family = gaussian, data = df_income_childs_sp, na.action = na.omit)
model_income_childs_sm = glm(formula = Childs_Time_SM ~ Income, family = gaussian, data = df_income_childs_sm, na.action = na.omit)
model_income_childs_vg = glm(formula = Childs_Time_VG ~ Income, family = gaussian, data = df_income_childs_vg, na.action = na.omit)

model_parents_sp_childs_sp = glm(formula = Childs_Time_SP ~ Parents_Time_SP, family = gaussian, data = df_parents_sp_childs_sp, na.action = na.omit)
model_parents_sm_childs_sm = glm(formula = Childs_Time_SM ~ Parents_Time_SM, family = gaussian, data = df_parents_sm_childs_sm, na.action = na.omit)
model_parents_vg_childs_vg = glm(formula = Childs_Time_VG ~ Parents_Time_VG, family = gaussian, data = df_parents_vg_childs_vg, na.action = na.omit)


# Summary of the 9 models
summary(model_income_parents_sp)
summary(model_income_parents_sm)
summary(model_income_parents_vg)

summary(model_income_childs_sp)
summary(model_income_childs_sm)
summary(model_income_childs_vg)

summary(model_parents_sp_childs_sp)
summary(model_parents_sm_childs_sm)
summary(model_parents_vg_childs_vg)


# Sequence of X values used in prediction
xvalues_income_parents_sp = seq(1, 9, 0.01)   # Range of income values least to greatest (1, 9) by a range of 0.01
xvalues_income_parents_sm = seq(1, 9, 0.01)
xvalues_income_parents_vg = seq(1, 9, 0.01)

xvalues_income_childs_sp = seq(1, 9, 0.01)
xvalues_income_childs_sm = seq(1, 9, 0.01)
xvalues_income_childs_vg = seq(1, 9, 0.01)

xvalues_parents_sp_childs_sp = seq(1, 3, 0.01)  # Range of income values least to greatest (1, 3) by a range of 0.01
xvalues_parents_sm_childs_sm = seq(1, 3, 0.01)
xvalues_parents_vg_childs_vg = seq(1, 3, 0.01)

# Prediction, y values
predict_income_parents_sp = predict(object = model_income_parents_sp, newdata = list(Income=xvalues_income_parents_sp))
predict_income_parents_sm = predict(object = model_income_parents_sm, newdata = list(Income=xvalues_income_parents_sm))
predict_income_parents_vg = predict(object = model_income_parents_vg, newdata = list(Income=xvalues_income_parents_vg))

predict_income_childs_sp = predict(object = model_income_childs_sp, newdata = list(Income=xvalues_income_childs_sp))
predict_income_childs_sm = predict(object = model_income_childs_sm, newdata = list(Income=xvalues_income_childs_sm))
predict_income_childs_vg = predict(object = model_income_childs_vg, newdata = list(Income=xvalues_income_childs_vg))

predict_parents_sp_childs_sp = predict(object = model_parents_sp_childs_sp, newdata = list(Parents_Time_SP=xvalues_parents_sp_childs_sp))
predict_parents_sm_childs_sm = predict(object = model_parents_sm_childs_sm, newdata = list(Parents_Time_SM=xvalues_parents_sm_childs_sm))
predict_parents_vg_childs_vg = predict(object = model_parents_vg_childs_vg, newdata = list(Parents_Time_VG=xvalues_parents_vg_childs_vg))


# Plotting Logistical Regression Lines
plot(df_mapped_income_time$Income, df_mapped_income_time$Parents_Time_SP, pch = 16, xlab = "Parent's Income", ylab = "Parent's Time on Smartphone")
lines(xvalues_income_parents_sp, predict_income_parents_sp, col = 'red')

plot(df_mapped_income_time$Income, df_mapped_income_time$Parents_Time_SM, pch = 16, xlab = "Parent's Income", ylab = "Parent's Time on Social Media")
lines(xvalues_income_parents_sm, predict_income_parents_sm, col = 'red')

plot(df_mapped_income_time$Income, df_mapped_income_time$Parents_Time_VG, pch = 16, xlab = "Parent's Income", ylab = "Parent's Time on Video Games")
lines(xvalues_income_parents_vg, predict_income_parents_vg, col = 'red')



plot(df_mapped_income_time$Income, df_mapped_income_time$Childs_Time_SP, pch = 16, xlab = "Parent's Income", ylab = "Child's Time on Smartphone")
lines(xvalues_income_childs_sp, predict_income_childs_sp, col = 'red')

plot(df_mapped_income_time$Income, df_mapped_income_time$Childs_Time_SM, pch = 16, xlab = "Parent's Income", ylab = "Child's Time on Social Media")
lines(xvalues_income_childs_sm, predict_income_childs_sm, col = 'red')

plot(df_mapped_income_time$Income, df_mapped_income_time$Childs_Time_VG, pch = 16, xlab = "Parent's Income", ylab = "Child's Time on Video Games")
lines(xvalues_income_childs_vg, predict_income_childs_vg, col = 'red')



plot(df_mapped_income_time$Parents_Time_SP, df_mapped_income_time$Childs_Time_SP, pch = 16, xlab = "Parent's Time on Smartphone", ylab = "Child's Time on Smartphone")
lines(xvalues_parents_sp_childs_sp, predict_parents_sp_childs_sp, col = 'red')

plot(df_mapped_income_time$Parents_Time_SM, df_mapped_income_time$Childs_Time_SM, pch = 16, xlab = "Parent's Time on Social Media", ylab = "Child's Time on Social Media")
lines(xvalues_parents_sm_childs_sm, predict_parents_sm_childs_sm, col = 'red')

plot(df_mapped_income_time$Parents_Time_VG, df_mapped_income_time$Childs_Time_VG, pch = 16, xlab = "Parent's Time on Video Games", ylab = "Child's Time on Video Games")
lines(xvalues_parents_vg_childs_vg, predict_parents_vg_childs_vg, col = 'red')

