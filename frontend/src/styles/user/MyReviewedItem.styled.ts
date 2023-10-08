import styled from 'styled-components'

export const ReviewedCard = styled.div`
  display: flex;
  align-items: center;
  gap: 10px;
  padding: 20px;
  border-radius: 10px;
  box-shadow: 0px 2px 6px 2px rgba(89, 97, 104, 0.1);
`

export const ReviewedImg = styled.img`
  width: 90px;
  height: 90px;
  border-radius: 10px;
  background-color: yellow;
`

export const ReviewedInfoContainer = styled.div`
  height: 80px;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
`

export const ReviewedTopInfo = styled.div`
  display: flex;
  align-items: center;
  gap: 20px;
`

export const ReviewedUserInfo = styled.div`
  display: flex;
  gap: 10px;
  align-items: center;
`

export const ReviewedUserImg = styled.img`
  width: 30px;
  height: 30px;
  border-radius: 100%;
  background-color: aliceblue;
`

export const ReviewedUserName = styled.span`
  font-weight: bold;
  display: -webkit-box;
  overflow: hidden;
  word-break: break-all;
  -webkit-box-orient: vertical;
  -webkit-line-clamp: 1;
`

export const ReviewedContent = styled.p`
  display: -webkit-box;
  overflow: hidden;
  word-break: break-all;
  -webkit-box-orient: vertical;
  -webkit-line-clamp: 1;
`
export const ReviewedAt = styled.p`
  font-size: 1.2rem;
`
