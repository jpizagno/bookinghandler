states = {
    'AK': 'Alaska',
    'AL': 'Alabama',
    'AR': 'Arkansas',
    'AS': 'American Samoa',
    'AZ': 'Arizona',
    'CA': 'California',
    'CO': 'Colorado',
    'CT': 'Connecticut',
    'DC': 'District of Columbia',
    'DE': 'Delaware',
    'FL': 'Florida',
    'GA': 'Georgia',
    'GU': 'Guam',
    'HI': 'Hawaii',
    'IA': 'Iowa',
    'ID': 'Idaho',
    'IL': 'Illinois',
    'IN': 'Indiana',
    'KS': 'KKansas',
    'KY': 'Kentucky',
    'LA': 'Louisiana',
    'MA': 'Massachusetts',
    'MD': 'Maryland',
    'ME': 'Maine',
    'MI': 'Michigan',
    'MN': 'Minnesota',
    'MO': 'Missouri',
    'MP': 'Northern Mariana Islands',
    'MS': 'Mississippi',
    'MT': 'Montana',
    'NA': 'National',
    'NC': 'North Carolina',
    'ND': 'North Dakota',
    'NE': 'Nebraska',
    'NH': 'New Hampshire',
    'NJ': 'New Jersey',
    'NM': 'New Mexico',
    'NV': 'Nevada',
    'NY': 'New York',
    'OH': 'Ohio',
    'OK': 'Oklahoma',
    'OR': 'Oregon',
    'PA': 'Pennsylvania',
    'PR': 'Puerto Rico',
    'RI': 'Rhode Island',
    'SC': 'South Carolina',
    'SD': 'South Dakota',
    'TN': 'Tennessee',
    'TX': 'Texas',
    'UT': 'Utah',
    'VA': 'Virginia',
    'VI': 'Virgin Islands',
    'VT': 'Vermont',
    'WA': 'Washington',
    'WI': 'Wisconsin',
    'WV': 'West Virgini',
    'WY': 'Wyoming'
}



state_coordinates_dict = {}

abbr_here = "ERROR"

file = open("state_longitudes.txt",'r')
for line in file.readlines():
    line = line.replace("\n","")
    if line.count("LATITUDE") > 0:
        # found a new state
        for abbr,state in states.items():
            if line.lower().count(state.lower()) > 0:
                #print abbr,state,line
                if abbr not in state_coordinates_dict.keys():
                    state_coordinates_dict[abbr] = []
                    abbr_here = abbr
    else:
        ## have a long / lattitude for this abbr_here state
        #import pdb
        #pdb.set_trace()
        fields = line.split('\t')
        latt = float(fields[1].replace("\xc2\xb0","")) + float(fields[2].replace("' ","").replace("N","").replace("S",""))/60.         
        long = -1.0 * (float( fields[3].replace("\xc2\xb0",""))  + float(fields[4].replace("' ","").replace("W","").replace("E",""))/60. )
        state_coordinates_dict[abbr_here].append(str(long) + ":" + str(latt))
file.close()

print "states_coordinates_dict = { "
for state in state_coordinates_dict.keys():
    value = ";".join(state_coordinates_dict[state])
    outline = "'" + state + "':'" + value + "'"
    print outline
print "}"


