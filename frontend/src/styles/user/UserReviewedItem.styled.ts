import styled from 'styled-components'
import { breakpoints, palette, typography } from '../../constants/Styles'

export const ReviewedCard = styled.div`
  display: flex;
  align-items: center;
  gap: 1rem;
  padding: 2rem;
  border-radius: 1rem;
  box-shadow: 0px 2px 6px 2px rgba(89, 97, 104, 0.1);
  cursor: pointer;
`

export const ReviewedImg = styled.img`
  width: 9rem;
  height: 9rem;
  border-radius: 1rem;
  background-color: aliceblue;
  object-fit: cover;
`

export const ReviewedInfoContainer = styled.div`
  height: 8rem;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
`

export const ReviewedTopInfo = styled.div`
  display: flex;
  align-items: center;
  gap: 1.5rem;
`

export const ReviewedUserInfo = styled.div`
  display: flex;
  gap: 1rem;
  align-items: center;
`

export const ReviewedUserImg = styled.img`
  width: 3rem;
  height: 3rem;
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
  font-size: ${typography.mobile.desc};

  ${breakpoints.large} {
    font-size: ${typography.web.desc};
  }
`

export const NoReviewedCard = styled.div`
  display: flex;
  justify-content: center;
  align-items: center;
  height: 14rem;
  border-radius: 1rem;
  color: ${palette.divider};
  box-shadow: 0px 2px 6px 2px rgba(89, 97, 104, 0.1);
`
