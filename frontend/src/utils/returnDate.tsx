const formatDateTime = (dateTimeStr: string) => {
  const formattedDate = new Date(dateTimeStr)

  return `${formattedDate.getFullYear()}.${
    formattedDate.getMonth() + 1
  }.${formattedDate.getDate()} 
  ${formattedDate.getHours()}시 ${formattedDate.getMinutes()}분
  `
}

export default formatDateTime
