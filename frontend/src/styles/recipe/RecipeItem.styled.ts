import styled from 'styled-components'
import { breakpoints, palette } from '../../constants/Styles'

export const Container = styled.div`
  display: flex;
  align-items: center;
  width: 100%;
  margin: 0 auto 1rem;
  padding: 1rem;
  border: 1px solid ${palette.divider};
  border-radius: 1rem;

  ${breakpoints.large} {
    width: clamp(40rem, 50vw, 50vw);
    flex-direction: column;
    padding: 0;
    margin: 2rem 1.5rem;
  }
`

export const Title = styled.span`
  font-size: 1.4rem;
`
export const Nickname = styled.span`
  font-size: 1.2rem;
  margin-left: 3%;
  color: ${palette.textSecondary};
`

export const Introduce = styled.span`
  font-size: 1.2rem;
  color: ${palette.textSecondary};
  margin-top: 0.5rem;
  text-overflow: ellipsis;
  white-space: nowrap;
  overflow: hidden;
`
export const LikeButton = styled.button`
  display: flex;
  align-items: center;
`
export const LikeCount = styled.span`
  font-size: 1.2rem;
  color: ${palette.textPrimary};
  margin-left: 0.5rem;
`

export const Image = styled.img`
  width: 8rem;
  height: 8rem;
  background-color: ${palette.divider};
  margin-right: 1rem;
  border-radius: 1rem;

  ${breakpoints.large} {
    width: 100%;
    height: 100%;
    object-fit: cover;
    border-radius: 1rem 1rem 0 0;
    margin: 0;
  }

`

export const WrapContent = styled.div`
  width: 100%;
  display: flex;
  flex-direction: column;

  ${breakpoints.large} {
    width: 90%;
    padding: 5%;
  }
`

export const FlexRowJustiBet = styled.div`
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: space-between;
`

export const FlexRow = styled.div`
  flex: 1;
  display: flex;
  align-items: center;
`
